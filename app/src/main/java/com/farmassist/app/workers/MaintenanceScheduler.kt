package com.farmassist.app.workers

import android.content.Context
import androidx.work.Data
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.farmassist.app.data.local.dao.FarmAssistDao
import java.util.concurrent.TimeUnit

class MaintenanceScheduler(private val context: Context, private val dao: FarmAssistDao) {

    suspend fun scheduleForCrop(cropName: String) {
        val workManager = WorkManager.getInstance(context)

        val schedule = dao.getScheduleForCrop(cropName)
        schedule.forEach { task ->
            scheduleNotification(workManager, cropName, task.activity, task.day)
        }

        val fertilizers = dao.getFertilizerForCrop(cropName)
        fertilizers.forEach { feed ->
            scheduleNotification(workManager, cropName, "Apply Fertilizer: ${feed.fertilizer}", feed.day)
        }

        val irrigation = dao.getIrrigationForCrop(cropName)
        if (irrigation != null) {
            for (i in 1..5) {
                scheduleNotification(workManager, cropName, "Irrigate Crop", irrigation.interval_days * i)
            }
        }
    }

    private fun scheduleNotification(workManager: WorkManager, crop: String, task: String, delayDays: Int) {
        val data = Data.Builder()
            .putString("title", "Farm Assist: $crop Reminder")
            .putString("message", "Task for today: $task")
            .build()

        val request = OneTimeWorkRequestBuilder<NotificationWorker>()
            .setInitialDelay(delayDays.toLong(), TimeUnit.DAYS)
            .setInputData(data)
            .build()

        workManager.enqueue(request)
    }
}
