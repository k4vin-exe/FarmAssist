package com.farmassist.util

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.os.Build
import androidx.core.content.ContextCompat
import com.google.android.gms.location.*
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withTimeoutOrNull
import java.util.Locale
import kotlin.coroutines.resume

class LocationHelper(private val context: Context) {

    private val fusedClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(context)

    /**
     * Returns the best available location using a 3-layer fallback:
     * 1. getCurrentLocation (fresh GPS fix, 8-second timeout)
     * 2. getLastLocation (cached, instant – works indoors)
     * 3. null → caller should show manual district picker
     */
    suspend fun getCurrentLocation(): Location? {
        if (!hasPermission()) return null

        return try {
            // Layer 1: Try a fresh high-accuracy fix with a strict 8s timeout
            val fresh = withTimeoutOrNull(8_000L) {
                fusedClient.getCurrentLocation(
                    Priority.PRIORITY_HIGH_ACCURACY,
                    null          // no cancellation token
                ).await()
            }
            if (fresh != null) return fresh

            // Layer 2: Balanced accuracy fix with 5s timeout (works better indoors)
            val balanced = withTimeoutOrNull(5_000L) {
                fusedClient.getCurrentLocation(
                    Priority.PRIORITY_BALANCED_POWER_ACCURACY,
                    null
                ).await()
            }
            if (balanced != null) return balanced

            // Layer 3: Last known cached location (always instant, may be stale)
            fusedClient.lastLocation.await()

        } catch (e: Exception) {
            // Final fallback: try lastLocation safely
            try { fusedClient.lastLocation.await() } catch (_: Exception) { null }
        }
    }

    /**
     * Converts a GPS coordinate to a Tamil Nadu district name.
     * Uses the modern listener API on Android 13+ to avoid the deprecated
     * blocking Geocoder.getFromLocation() that often times-out on physical devices.
     */
    suspend fun getDistrictFromLocation(location: Location): String? {
        return try {
            val geocoder = Geocoder(context, Locale.ENGLISH)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                // Android 13+ — non-blocking listener-based API
                suspendCancellableCoroutine { cont ->
                    geocoder.getFromLocation(
                        location.latitude, location.longitude, 1,
                        Geocoder.GeocodeListener { addresses ->
                            if (!addresses.isNullOrEmpty()) {
                                val a = addresses[0]
                                // subAdminArea = district (e.g. "Coimbatore District")
                                // adminArea    = state  (e.g. "Tamil Nadu")
                                cont.resume(
                                    a.subAdminArea
                                        ?: a.locality
                                        ?: a.getAddressLine(0)
                                )
                            } else {
                                cont.resume(null)
                            }
                        }
                    )
                }
            } else {
                // Android 12 and below — use classic blocking API
                @Suppress("DEPRECATION")
                val addresses = geocoder.getFromLocation(location.latitude, location.longitude, 1)
                if (!addresses.isNullOrEmpty()) {
                    val a = addresses[0]
                    a.subAdminArea ?: a.locality ?: a.getAddressLine(0)
                } else null
            }
        } catch (e: Exception) {
            null // Geocoder unreachable → caller uses GPS→nearest-district math fallback
        }
    }

    private fun hasPermission(): Boolean =
        ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED ||
        ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED
}
