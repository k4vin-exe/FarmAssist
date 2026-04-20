package com.farmassist.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.farmassist.ui.screens.*
import com.farmassist.util.SessionManager

import android.content.Context
import com.farmassist.data.local.dao.FarmDao
import com.farmassist.data.local.dao.NewsDao
import com.farmassist.data.remote.WeatherApi
import com.farmassist.data.remote.NewsApi
import com.farmassist.util.LocationHelper
import com.farmassist.ui.viewmodels.*
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

@Composable
fun FarmAssistNavGraph(
    sessionManager: SessionManager,
    farmDao: FarmDao,
    locationHelper: LocationHelper,
    weatherApi: WeatherApi,
    newsDao: NewsDao,
    newsApi: NewsApi,
    context: Context
) {
    val navController = rememberNavController()

    // Determine start destination
    val startDestination = "profile_selection"

    NavHost(navController = navController, startDestination = startDestination) {
        
        composable("profile_selection") {
            ProfileSelectionScreen(
                sessionManager = sessionManager,
                onGuestSelected = {
                    navController.navigate("dashboard/true") {
                        popUpTo("profile_selection") { inclusive = true }
                    }
                },
                onProfileSelected = {
                    navController.navigate("pin_login")
                },
                onCreateProfile = {
                    navController.navigate("language")
                }
            )
        }

        composable("language") {
            LanguageScreen(onLanguageSelected = { lang ->
                sessionManager.saveLanguage(lang)
                navController.navigate("profile_setup") {
                    popUpTo("language") { inclusive = true }
                }
            })
        }

        composable("profile_setup") {
            ProfileSetupScreen(onProfileSaved = { name, pin ->
                sessionManager.saveProfile(name, pin)
                navController.navigate("dashboard/false") {
                    popUpTo("profile_setup") { inclusive = true }
                }
            })
        }

        composable("pin_login") {
            PinLoginScreen(
                savedPin = sessionManager.getUserPin() ?: "",
                onLoginSuccess = {
                    navController.navigate("dashboard/false") {
                        popUpTo("pin_login") { inclusive = true }
                    }
                }
            )
        }

        composable("dashboard/{isGuest}") { backStackEntry ->
            val isGuestArg = backStackEntry.arguments?.getString("isGuest") ?: "false"
            DashboardScreen(
                userName = sessionManager.getUserName(),
                isGuest = isGuestArg.toBoolean(),
                onNavigate = { route -> navController.navigate(route) },
                onSettingsClick = { navController.navigate("settings") }
            )
        }

        composable("crop_estimation") { 
            val apiKey = "94d2507f60523cbf3bbcf70652fd3e22"
            val cropViewModel = remember { CropViewModel(farmDao, locationHelper, weatherApi, apiKey) }
            CropEstimationScreen(viewModel = cropViewModel) 
        }
        composable("cost_yield") { 
            val costYieldViewModel = remember { CostYieldViewModel(farmDao) }
            CostYieldScreen(viewModel = costYieldViewModel) 
        }
        composable("maintenance") { 
            val apiKey = "94d2507f60523cbf3bbcf70652fd3e22"
            val maintenanceViewModel = remember { MaintenanceViewModel(farmDao, context, locationHelper, weatherApi, apiKey, sessionManager) }
            MaintenanceScreen(viewModel = maintenanceViewModel) 
        }
        composable("terrace") { 
            val infoViewModel = remember { InfoViewModel(farmDao, sessionManager) }
            TerraceFarmingScreen(viewModel = infoViewModel) 
        }
        composable("waste") { 
            val infoViewModel = remember { InfoViewModel(farmDao, sessionManager) }
            WasteManagementScreen(viewModel = infoViewModel) 
        }
        composable("news") { 
            val newsRepository = remember { com.farmassist.data.repository.NewsRepository(newsDao, newsApi) }
            val newsViewModel = remember { NewsViewModel(newsRepository) }
            NewsScreen(viewModel = newsViewModel) 
        }
        composable("schemes") { 
            val infoViewModel = remember { InfoViewModel(farmDao, sessionManager) }
            SchemesScreen(viewModel = infoViewModel) 
        }
        composable("settings") {
            SettingsScreen(sessionManager = sessionManager)
        }
    }
}
