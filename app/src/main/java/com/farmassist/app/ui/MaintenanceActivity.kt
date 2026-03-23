package com.farmassist.app.ui

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.farmassist.app.R
import com.farmassist.app.data.local.database.FarmAssistDatabase
import com.farmassist.app.workers.MaintenanceScheduler
import kotlinx.coroutines.launch

class MaintenanceActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maintenance)

        val etCrop = findViewById<EditText>(R.id.etCropName)
        val btnAdd = findViewById<Button>(R.id.btnPlantCrop)
        val listView = findViewById<ListView>(R.id.listViewTimeline)

        val database = FarmAssistDatabase.getDatabase(this)
        val dao = database.farmAssistDao()
        val scheduler = MaintenanceScheduler(this, dao)

        btnAdd.setOnClickListener {
            val crop = etCrop.text.toString().trim()
            if (crop.isNotEmpty()) {
                lifecycleScope.launch {
                    val schedule = dao.getScheduleForCrop(crop)
                    if (schedule.isEmpty()) {
                        Toast.makeText(this@MaintenanceActivity, "Crop schedule not found in database", Toast.LENGTH_SHORT).show()
                    } else {
                        scheduler.scheduleForCrop(crop)
                        Toast.makeText(this@MaintenanceActivity, "Background maintenance tasks scheduled!", Toast.LENGTH_SHORT).show()
                        
                        // Populate timeline view
                        val items = schedule.map { "Day ${it.day}: ${it.activity}" }
                        listView.adapter = ArrayAdapter(this@MaintenanceActivity, android.R.layout.simple_list_item_1, items)
                    }
                }
            }
        }
    }
}
