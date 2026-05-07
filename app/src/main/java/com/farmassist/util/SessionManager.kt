package com.farmassist.util

import android.content.Context
import android.content.SharedPreferences

class SessionManager(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("farm_assist_prefs", Context.MODE_PRIVATE)

    fun saveLanguage(lang: String) {
        prefs.edit().putString("language", lang).apply()
    }

    fun getLanguage(): String {
        return prefs.getString("language", "en") ?: "en"
    }

    fun saveProfile(name: String, pin: String) {
        prefs.edit().apply {
            putString("user_name", name)
            putString("user_pin", pin)
            putBoolean("is_registered", true)
        }.apply()
    }

    fun getUserName(): String {
        return prefs.getString("user_name", "Farmer") ?: "Farmer"
    }

    fun getUserPin(): String? {
        return prefs.getString("user_pin", null)
    }

    fun isRegistered(): Boolean {
        return prefs.getBoolean("is_registered", false)
    }

    fun addPlantedCrop(cropName: String) {
        val current = getPlantedCrops().toMutableSet()
        current.add(cropName)
        prefs.edit().putStringSet("planted_crops", current).apply()
        // Record planting date
        prefs.edit().putLong("planted_date_$cropName", System.currentTimeMillis()).apply()
    }

    fun removePlantedCrop(cropName: String) {
        val current = getPlantedCrops().toMutableSet()
        current.remove(cropName)
        prefs.edit().putStringSet("planted_crops", current).apply()
        prefs.edit().remove("planted_date_$cropName").apply()
    }

    fun getPlantedCrops(): Set<String> {
        return prefs.getStringSet("planted_crops", emptySet()) ?: emptySet()
    }

    fun addTerraceCrop(cropName: String) {
        val current = getTerraceCrops().toMutableSet()
        current.add(cropName)
        prefs.edit().putStringSet("terrace_crops", current).apply()
        prefs.edit().putLong("terrace_date_$cropName", System.currentTimeMillis()).apply()
    }

    fun removeTerraceCrop(cropName: String) {
        val current = getTerraceCrops().toMutableSet()
        current.remove(cropName)
        prefs.edit().putStringSet("terrace_crops", current).apply()
        prefs.edit().remove("terrace_date_$cropName").apply()
    }

    fun getTerraceCrops(): Set<String> {
        return prefs.getStringSet("terrace_crops", emptySet()) ?: emptySet()
    }

    /** Returns planting date in millis, or 0 if not recorded */
    fun getPlantedDate(cropName: String): Long {
        return prefs.getLong("planted_date_$cropName", 0L)
    }

    /** Returns terrace planting date in millis, or 0 if not recorded */
    fun getTerracePlantedDate(cropName: String): Long {
        return prefs.getLong("terrace_date_$cropName", 0L)
    }

    /** Returns the day number (1-based) since the crop was planted */
    fun getDaysSincePlanted(cropName: String): Int {
        val date = getPlantedDate(cropName)
        if (date == 0L) return 0
        val diffMs = System.currentTimeMillis() - date
        return ((diffMs / (1000 * 60 * 60 * 24)) + 1).toInt()
    }

    /** Returns the day number (1-based) since the terrace crop was planted */
    fun getDaysSinceTerracePlanted(cropName: String): Int {
        val date = getTerracePlantedDate(cropName)
        if (date == 0L) return 0
        val diffMs = System.currentTimeMillis() - date
        return ((diffMs / (1000 * 60 * 60 * 24)) + 1).toInt()
    }
}
