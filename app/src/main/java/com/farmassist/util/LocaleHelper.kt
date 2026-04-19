package com.farmassist.util

import android.content.Context
import android.content.res.Configuration
import java.util.Locale

object LocaleHelper {

    /**
     * Wraps the given context with the saved locale.
     * Call this in Activity.attachBaseContext() so every string resource
     * lookup uses the correct language from the very first frame.
     */
    fun applyLocale(context: Context): Context {
        val lang = SessionManager(context).getLanguage()
        return updateLocale(context, lang)
    }

    /**
     * Immediately switches the locale and returns an updated context.
     * After calling this, restart the Activity for full effect.
     */
    fun updateLocale(context: Context, language: String): Context {
        val locale = Locale(language)
        Locale.setDefault(locale)
        val config = Configuration(context.resources.configuration)
        config.setLocale(locale)
        return context.createConfigurationContext(config)
    }
}
