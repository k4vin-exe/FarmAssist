package com.farmassist.app.utils

import android.content.Context
import android.content.SharedPreferences

class SharedPreferencesHelper(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("FarmAssistPrefs", Context.MODE_PRIVATE)

    var language: String
        get() = prefs.getString("LANGUAGE", "en") ?: "en"
        set(value) = prefs.edit().putString("LANGUAGE", value).apply()

    var userPin: String?
        get() = prefs.getString("USER_PIN", null)
        set(value) = prefs.edit().putString("USER_PIN", value).apply()

    var landSizeAcres: Float
        get() = prefs.getFloat("LAND_SIZE", 0f)
        set(value) = prefs.edit().putFloat("LAND_SIZE", value).apply()

    var district: String?
        get() = prefs.getString("DISTRICT", null)
        set(value) = prefs.edit().putString("DISTRICT", value).apply()
        
    var isFirstLaunch: Boolean
        get() = prefs.getBoolean("FIRST_LAUNCH", true)
        set(value) = prefs.edit().putBoolean("FIRST_LAUNCH", value).apply()
}
