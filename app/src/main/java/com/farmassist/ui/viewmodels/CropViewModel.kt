package com.farmassist.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.farmassist.data.local.dao.FarmDao
import com.farmassist.data.local.model.Crop
import com.farmassist.data.local.model.DistrictSoil
import com.farmassist.data.remote.WeatherApi
import com.farmassist.util.LocationHelper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.Calendar

sealed class CropEstimationState {
    object Idle : CropEstimationState()
    object Loading : CropEstimationState()
    data class Success(
        val season: String,
        val soil: String,
        val temp: Int,
        val recommendations: List<Crop>,
        val fromLocation: Boolean,
        val defaultUsed: Boolean
    ) : CropEstimationState()
    data class Error(val message: String) : CropEstimationState()
}

class CropViewModel(
    private val farmDao: FarmDao,
    private val locationHelper: LocationHelper,
    private val weatherApi: WeatherApi,
    private val apiKey: String = "PLACEHOLDER"
) : ViewModel() {

    private val _uiState = MutableStateFlow<CropEstimationState>(CropEstimationState.Idle)
    val uiState: StateFlow<CropEstimationState> = _uiState.asStateFlow()

    val allDistricts: StateFlow<List<DistrictSoil>> = farmDao.getAllDistricts()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun setError(message: String) {
        _uiState.value = CropEstimationState.Error(message)
    }

    private fun getCurrentSeason(): String {
        val month = Calendar.getInstance().get(Calendar.MONTH) + 1 // 1-12
        return when (month) {
            in 6..9 -> "Kharif"
            in 10..12, in 1..2 -> "Rabi"
            else -> "Zaid"
        }
    }

    fun estimateCropAuto() {
        viewModelScope.launch {
            _uiState.value = CropEstimationState.Loading
            try {
                // Get location
                val location = locationHelper.getCurrentLocation()

                // Load districts directly from DAO (avoids StateFlow race condition on startup)
                val districts = farmDao.getAllDistricts()
                    .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())
                    .value
                    .ifEmpty {
                        // Fallback: collect first emission from the DAO flow
                        var loaded = emptyList<com.farmassist.data.local.model.DistrictSoil>()
                        val job = viewModelScope.launch {
                            farmDao.getAllDistricts().collect {
                                if (it.isNotEmpty()) { loaded = it }
                            }
                        }
                        kotlinx.coroutines.delay(2000)
                        job.cancel()
                        loaded
                    }

                if (districts.isEmpty()) {
                    _uiState.value = CropEstimationState.Error(
                        "District data not ready yet. Please select your district from the dropdown, or restart the app."
                    )
                    return@launch
                }

                val rawDistrictName = if (location != null)
                    locationHelper.getDistrictFromLocation(location)
                else null

                var districtInfo = districts.find {
                    rawDistrictName?.contains(it.district, ignoreCase = true) == true
                }

                // GPS coordinate → nearest district (works even if Geocoder fails)
                if (districtInfo == null && location != null) {
                    districtInfo = districts.minByOrNull { d ->
                        Math.pow(d.lat - location.latitude, 2.0) +
                        Math.pow(d.lng - location.longitude, 2.0)
                    }
                }

                if (districtInfo == null) {
                    _uiState.value = CropEstimationState.Error(
                        "Could not detect your district. Please select it manually from the dropdown."
                    )
                    return@launch
                }

                val soilType = districtInfo.soil
                var temp = districtInfo.defaultTemp
                var isDefault = true

                if (location != null && apiKey != "PLACEHOLDER") {
                    try {
                        val weatherResponse = weatherApi.getCurrentWeather(
                            location.latitude, location.longitude, apiKey, "metric"
                        )
                        temp = weatherResponse.main.temp.toInt()
                        isDefault = false
                    } catch (_: Exception) { }
                }

                val season = getCurrentSeason()
                val recommendedCrops = farmDao.getRecommendedCrops(soilType, season, temp)

                _uiState.value = CropEstimationState.Success(
                    season = season,
                    soil = soilType,
                    temp = temp,
                    recommendations = recommendedCrops,
                    fromLocation = location != null,
                    defaultUsed = isDefault
                )
            } catch (e: Exception) {
                _uiState.value = CropEstimationState.Error(
                    e.message ?: "Unknown error. Please try selecting your district manually."
                )
            }
        }
    }

    fun estimateCropManual(districtName: String) {
        viewModelScope.launch {
            _uiState.value = CropEstimationState.Loading
            try {
                val districtInfo = farmDao.getDistrictInfo(districtName)
                if (districtInfo == null) {
                    _uiState.value = CropEstimationState.Error("District data missing")
                    return@launch
                }

                val soilType = districtInfo.soil
                val temp = districtInfo.defaultTemp // Guaranteed offline value
                val season = getCurrentSeason()
                
                val recommendedCrops = farmDao.getRecommendedCrops(soilType, season, temp)

                _uiState.value = CropEstimationState.Success(
                    season = season,
                    soil = soilType,
                    temp = temp,
                    recommendations = recommendedCrops,
                    fromLocation = false,
                    defaultUsed = true
                )
            } catch (e: Exception) {
                _uiState.value = CropEstimationState.Error(e.message ?: "Unknown error")
            }
        }
    }
}
