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
                val location = locationHelper.getCurrentLocation()
                val rawDistrictName = if (location != null) locationHelper.getDistrictFromLocation(location) else ""

                // Match "Coimbatore District" or just "Coimbatore" back to DB key safely
                var districtInfo = allDistricts.value.find { rawDistrictName?.contains(it.district, ignoreCase = true) == true }
                
                // If Geocoder strictly rejected it (e.g., omitted Tiruvannamalai for Vettavalam), magically find the nearest district!
                if (districtInfo == null && location != null) {
                    val userLat = location.latitude
                    val userLng = location.longitude
                    
                    // Simple Euclidean Distance formula to map GPS directly to nearest tracked district
                    districtInfo = allDistricts.value.minByOrNull { dict ->
                        Math.pow(dict.lat - userLat, 2.0) + Math.pow(dict.lng - userLng, 2.0)
                    }
                }

                if (districtInfo == null) {
                    _uiState.value = CropEstimationState.Error("GPS couldn't map this coordinate to a primary district. Please select it manually from the dropdown!")
                    return@launch
                }

                val soilType = districtInfo.soil
                var temp = districtInfo.defaultTemp
                var isDefault = true

                // Try live openweather metrics, fallback to DB offline temp silently
                if (location != null && apiKey != "PLACEHOLDER") {
                    try {
                        val weatherResponse = weatherApi.getCurrentWeather(location.latitude, location.longitude, apiKey, "metric")
                        temp = weatherResponse.main.temp.toInt()
                        isDefault = false
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }

                val season = getCurrentSeason()
                val recommendedCrops = farmDao.getRecommendedCrops(soilType, season, temp)

                _uiState.value = CropEstimationState.Success(
                    season = season,
                    soil = soilType,
                    temp = temp,
                    recommendations = recommendedCrops,
                    fromLocation = true,
                    defaultUsed = isDefault
                )
            } catch (e: Exception) {
                _uiState.value = CropEstimationState.Error(e.message ?: "Unknown error")
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
