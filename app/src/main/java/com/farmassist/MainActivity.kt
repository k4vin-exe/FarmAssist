package com.farmassist

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.farmassist.util.LocaleHelper
import com.farmassist.util.SessionManager

class MainActivity : ComponentActivity() {
    private lateinit var sessionManager: SessionManager
    private lateinit var farmDatabase: com.farmassist.data.local.FarmDatabase
    private lateinit var farmDao: com.farmassist.data.local.dao.FarmDao
    private lateinit var newsDao: com.farmassist.data.local.dao.NewsDao
    private lateinit var locationHelper: com.farmassist.util.LocationHelper
    private lateinit var weatherApi: com.farmassist.data.remote.WeatherApi
    private lateinit var newsApi: com.farmassist.data.remote.NewsApi

    /**
     * Override attachBaseContext so the locale is applied BEFORE
     * any resources (including layouts) are loaded.
     */
    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(LocaleHelper.applyLocale(newBase))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sessionManager  = SessionManager(this)
        farmDatabase    = com.farmassist.data.local.FarmDatabase.getDatabase(this)
        farmDao         = farmDatabase.farmDao()
        newsDao         = farmDatabase.newsDao()
        locationHelper  = com.farmassist.util.LocationHelper(this)
        weatherApi      = com.farmassist.data.remote.WeatherApi.create()
        newsApi         = com.farmassist.data.remote.NewsApi.create()

        setContent {
            com.farmassist.ui.theme.FarmAssistTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = androidx.compose.material3.MaterialTheme.colorScheme.background
                ) {
                    com.farmassist.ui.navigation.FarmAssistNavGraph(
                        sessionManager = sessionManager,
                        farmDao        = farmDao,
                        locationHelper = locationHelper,
                        weatherApi     = weatherApi,
                        newsDao        = newsDao,
                        newsApi        = newsApi,
                        context        = this
                    )
                }
            }
        }
    }

    companion object {
        fun restartWithNewLocale(context: Context) {
            val intent = Intent(context, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            context.startActivity(intent)
        }
    }
}
