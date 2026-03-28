package com.farmassist

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.farmassist.util.SessionManager

class MainActivity : ComponentActivity() {
    private lateinit var sessionManager: SessionManager
    private lateinit var farmDatabase: com.farmassist.data.local.FarmDatabase
    private lateinit var farmDao: com.farmassist.data.local.dao.FarmDao
    private lateinit var locationHelper: com.farmassist.util.LocationHelper
    private lateinit var weatherApi: com.farmassist.data.remote.WeatherApi

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sessionManager = SessionManager(this)
        farmDatabase = com.farmassist.data.local.FarmDatabase.getDatabase(this)
        farmDao = farmDatabase.farmDao()
        locationHelper = com.farmassist.util.LocationHelper(this)
        weatherApi = com.farmassist.data.remote.WeatherApi.create()

        setContent {
            com.farmassist.ui.theme.FarmAssistTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = androidx.compose.material3.MaterialTheme.colorScheme.background
                ) {
                    com.farmassist.ui.navigation.FarmAssistNavGraph(
                        sessionManager = sessionManager,
                        farmDao = farmDao,
                        locationHelper = locationHelper,
                        weatherApi = weatherApi,
                        context = this
                    )
                }
            }
        }
    }
}
