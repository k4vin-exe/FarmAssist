package com.farmassist.app.domain.engine

import com.farmassist.app.data.local.dao.FarmAssistDao
import com.farmassist.app.data.local.entity.CropDataEntity
import com.farmassist.app.data.remote.RetrofitClient
import java.util.Calendar

class CropRecommendationEngine(private val dao: FarmAssistDao) {

    suspend fun getRecommendations(lat: Double, lon: Double, district: String): List<CropDataEntity> {
        val soilType = dao.getSoilByDistrict(district) ?: "Red"
        
        val weather = RetrofitClient.weatherApi.getCurrentWeather(lat, lon, RetrofitClient.WEATHER_API_KEY)
        val currentTemp = weather.main.temp.toInt()
        
        val currentSeason = getCurrentSeason()
        
        return dao.getRecommendedCrops(soilType, currentSeason, currentTemp)
    }

    private fun getCurrentSeason(): String {
        val month = Calendar.getInstance().get(Calendar.MONTH)
        return when (month) {
            in 2..4 -> "Summer"
            in 5..9 -> "Rainy"
            else -> "All"
        }
    }
}
