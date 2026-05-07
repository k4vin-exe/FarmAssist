package com.farmassist.ui.viewmodels

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.Data
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.farmassist.R
import com.farmassist.data.local.dao.FarmDao
import com.farmassist.data.local.model.*
import com.farmassist.util.LocationHelper
import com.farmassist.util.SessionManager
import com.farmassist.data.remote.WeatherApi
import com.farmassist.util.NotificationWorker
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

data class SmartAdvice(
    val title: String,
    val description: String,
    val isAlert: Boolean = false,
    val iconType: String = "Normal"
)

@android.annotation.SuppressLint("StaticFieldLeak")
class MaintenanceViewModel(
    private val dao: FarmDao, 
    private val context: Context,
    private val locationHelper: LocationHelper,
    private val weatherApi: WeatherApi,
    private val apiKey: String,
    private val sessionManager: SessionManager
) : ViewModel() {

    private val _plantedCrops = MutableStateFlow<Set<String>>(sessionManager.getPlantedCrops())
    val plantedCrops: StateFlow<Set<String>> = _plantedCrops.asStateFlow()

    private val _crops = MutableStateFlow<List<Crop>>(emptyList())
    val crops: StateFlow<List<Crop>> = _crops.asStateFlow()

    private val _schedules = MutableStateFlow<List<CropSchedule>>(emptyList())
    val schedules: StateFlow<List<CropSchedule>> = _schedules.asStateFlow()

    private val _fertilizers = MutableStateFlow<List<Fertilizer>>(emptyList())
    val fertilizers: StateFlow<List<Fertilizer>> = _fertilizers.asStateFlow()

    private val _pests = MutableStateFlow<List<Pest>>(emptyDestList())
    private fun <T> emptyDestList(): List<T> = emptyList()
    val pests: StateFlow<List<Pest>> = _pests.asStateFlow()

    private val _irrigation = MutableStateFlow<Irrigation?>(null)
    val irrigation: StateFlow<Irrigation?> = _irrigation.asStateFlow()

    private val _smartAdvice = MutableStateFlow<SmartAdvice?>(null)
    val smartAdvice: StateFlow<SmartAdvice?> = _smartAdvice.asStateFlow()

    init {
        viewModelScope.launch {
            _crops.value = dao.getAllCrops()
        }
        loadSmartIrrigation()
    }

    private fun loadSmartIrrigation() {
        viewModelScope.launch {
            try {
                val loc = locationHelper.getCurrentLocation() ?: return@launch
                val forecast = weatherApi.getForecast(loc.latitude, loc.longitude, apiKey, "metric")
                
                var rainCount = 0
                var stormCount = 0
                var highHeatCount = 0
                
                val forecastItems = forecast.list ?: emptyList() 
                forecastItems.take(24).forEach { item ->
                    val condition = item.weather.firstOrNull()?.main?.lowercase() ?: ""
                    val desc = item.weather.firstOrNull()?.description?.lowercase() ?: ""
                    val temp = item.main.temp
                    
                    if (condition.contains("storm") || condition.contains("extreme") || desc.contains("thunderstorm")) stormCount++
                    if (condition.contains("rain") || condition.contains("drizzle") || item.pop > 0.6) rainCount++
                    if (temp > 35.0) highHeatCount++
                }

                if (stormCount > 0) {
                    _smartAdvice.value = SmartAdvice(
                        title = context.getString(R.string.advice_storm_title),
                        description = context.getString(R.string.advice_storm_desc),
                        isAlert = true,
                        iconType = "Alert"
                    )
                } else if (rainCount > 3) {
                     _smartAdvice.value = SmartAdvice(
                        title = context.getString(R.string.advice_rain_title),
                        description = context.getString(R.string.advice_rain_desc),
                        isAlert = true,
                        iconType = "Rain"
                    )
                } else if (highHeatCount > 4) {
                     _smartAdvice.value = SmartAdvice(
                        title = context.getString(R.string.advice_heat_title),
                        description = context.getString(R.string.advice_heat_desc),
                        isAlert = false,
                        iconType = "Sun"
                    )
                } else {
                     _smartAdvice.value = SmartAdvice(
                        title = context.getString(R.string.advice_stable_title),
                        description = context.getString(R.string.advice_stable_desc),
                        iconType = "Normal"
                    )
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun loadCropDetails(cropName: String) {
        viewModelScope.launch {
            _schedules.value = dao.getScheduleForCrop(cropName)
            _fertilizers.value = dao.getFertilizersForCrop(cropName)
            _pests.value = dao.getPestsForCrop(cropName)
            _irrigation.value = dao.getIrrigationForCrop(cropName)
        }
    }

    fun togglePlantedCrop(cropName: String, isAdding: Boolean) {
        if (isAdding) {
            sessionManager.addPlantedCrop(cropName)
            scheduleReminders(cropName)
        } else {
            sessionManager.removePlantedCrop(cropName)
            WorkManager.getInstance(context).cancelAllWorkByTag("MAINTENANCE_$cropName")
        }
        _plantedCrops.value = sessionManager.getPlantedCrops()
    }

    fun getDaysSincePlanted(cropName: String): Int {
        return sessionManager.getDaysSincePlanted(cropName)
    }

    private fun scheduleReminders(cropName: String) {
        val workManager = WorkManager.getInstance(context)
        
        viewModelScope.launch {
            val scheduleList = dao.getScheduleForCrop(cropName)
            scheduleList.forEach { schedule ->
                val dayOf = schedule.day.toLong()
                val dayBefore = dayOf - 1L

                // 1. Day Of Reminder
                if (dayOf >= 0) {
                    val dataOf = Data.Builder()
                        .putString("title", "ACTION REQUIRED: $cropName Today (Day ${schedule.day})")
                        .putString("message", "Activity: ${schedule.activity}")
                        .build()
                        
                    val reqOf = OneTimeWorkRequestBuilder<NotificationWorker>()
                        .setInitialDelay(dayOf, TimeUnit.DAYS)
                        .setInputData(dataOf)
                        .addTag("MAINTENANCE_$cropName")
                        .build()
                        
                    workManager.enqueue(reqOf)
                }

                // 2. Day Before Reminder (only if the activity is more than 0 days out)
                if (dayBefore > 0) {
                    val dataBefore = Data.Builder()
                        .putString("title", "UPCOMING: $cropName Tomorrow (Day ${schedule.day})")
                        .putString("message", "Prepare for tomorrow: ${schedule.activity}")
                        .build()
                        
                    val reqBefore = OneTimeWorkRequestBuilder<NotificationWorker>()
                        .setInitialDelay(dayBefore, TimeUnit.DAYS)
                        .setInputData(dataBefore)
                        .addTag("MAINTENANCE_$cropName")
                        .build()
                        
                    workManager.enqueue(reqBefore)
                }
            }
        }
    }
}
