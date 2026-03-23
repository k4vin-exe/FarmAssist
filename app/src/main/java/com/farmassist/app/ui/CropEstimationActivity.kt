package com.farmassist.app.ui

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.farmassist.app.R
import com.farmassist.app.data.local.database.FarmAssistDatabase
import com.farmassist.app.domain.calculator.FinancialCalculator
import com.farmassist.app.domain.engine.CropRecommendationEngine
import com.farmassist.app.utils.SharedPreferencesHelper
import kotlinx.coroutines.launch

class CropEstimationActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crop_estimation)

        val prefs = SharedPreferencesHelper(this)
        val database = FarmAssistDatabase.getDatabase(this)
        val engine = CropRecommendationEngine(database.farmAssistDao())

        val tvInfo = findViewById<TextView>(R.id.tvProfileInfo)
        val listView = findViewById<ListView>(R.id.listViewCrops)
        val btnEstimate = findViewById<Button>(R.id.btnEstimate)

        tvInfo.text = "Profile: ${prefs.landSizeAcres} Acres in ${prefs.district ?: "Unknown"}"

        btnEstimate.setOnClickListener {
            lifecycleScope.launch {
                val dist = prefs.district ?: "Coimbatore"
                val landSize = prefs.landSizeAcres.toDouble()
                val lat = 11.0168
                val lon = 76.9558

                try {
                    val crops = engine.getRecommendations(lat, lon, dist)
                    val displayList = crops.map {
                        val totalCost = FinancialCalculator.calculateCost(it.cost_per_acre, landSize)
                        val totalYield = FinancialCalculator.calculateYield(it.yield_per_acre, landSize)
                        "${it.crop} (${it.season} - ${it.growing_days} Days)\nCost: ₹$totalCost\nEst. Yield: ${totalYield} kg"
                    }

                    if (displayList.isEmpty()) {
                        listView.adapter = ArrayAdapter(this@CropEstimationActivity, android.R.layout.simple_list_item_1, listOf("No crops found for current weather/soil."))
                    } else {
                        listView.adapter = ArrayAdapter(this@CropEstimationActivity, android.R.layout.simple_list_item_1, displayList)
                    }
                } catch (e: Exception) {
                    listView.adapter = ArrayAdapter(this@CropEstimationActivity, android.R.layout.simple_list_item_1, listOf("Error: ${e.message}"))
                }
            }
        }
    }
}
