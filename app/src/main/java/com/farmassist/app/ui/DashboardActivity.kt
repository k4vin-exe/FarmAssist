package com.farmassist.app.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.farmassist.app.R
import com.farmassist.app.data.remote.RetrofitClient
import com.farmassist.app.utils.SharedPreferencesHelper
import com.farmassist.app.utils.TTSManager
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.launch

class DashboardActivity : AppCompatActivity() {
    private lateinit var ttsManager: TTSManager
    private lateinit var prefs: SharedPreferencesHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        prefs = SharedPreferencesHelper(this)
        ttsManager = TTSManager(this, prefs.language)

        val tvWeather = findViewById<TextView>(R.id.tvWeather)
        val fabTTS = findViewById<FloatingActionButton>(R.id.fabTTS)

        val lat = 11.0168
        val lon = 76.9558

        lifecycleScope.launch {
            try {
                val weather = RetrofitClient.weatherApi.getCurrentWeather(lat, lon, RetrofitClient.WEATHER_API_KEY)
                tvWeather.text = "Temp: ${weather.main.temp}°C, Humidity: ${weather.main.humidity}%"
            } catch (e: Exception) {
                tvWeather.text = "Weather offline"
            }
        }

        fabTTS.setOnClickListener {
            ttsManager.speak("Welcome to Farm Assist Dashboard. Please select an option.")
        }

        findViewById<Button>(R.id.btnCropEst).setOnClickListener {
            startActivity(Intent(this, CropEstimationActivity::class.java))
        }

        findViewById<Button>(R.id.btnMaintenance).setOnClickListener {
            startActivity(Intent(this, MaintenanceActivity::class.java))
        }

        findViewById<Button>(R.id.btnTerrace).setOnClickListener {
            startActivity(GenericListActivity.newIntent(this, "TERRACE"))
        }

        findViewById<Button>(R.id.btnWaste).setOnClickListener {
            startActivity(GenericListActivity.newIntent(this, "WASTE"))
        }

        findViewById<Button>(R.id.btnNews).setOnClickListener {
            startActivity(GenericListActivity.newIntent(this, "NEWS"))
        }

        findViewById<Button>(R.id.btnSchemes).setOnClickListener {
            startActivity(GenericListActivity.newIntent(this, "SCHEMES"))
        }
    }

    override fun onDestroy() {
        ttsManager.shutdown()
        super.onDestroy()
    }
}
