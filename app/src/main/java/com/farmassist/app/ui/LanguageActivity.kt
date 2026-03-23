package com.farmassist.app.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.farmassist.app.R
import com.farmassist.app.utils.SharedPreferencesHelper

class LanguageActivity : AppCompatActivity() {
    private lateinit var prefs: SharedPreferencesHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        prefs = SharedPreferencesHelper(this)

        if (!prefs.isFirstLaunch) {
            val intent = if (prefs.userPin == null) {
                Intent(this, AuthActivity::class.java)
            } else {
                Intent(this, DashboardActivity::class.java)
            }
            startActivity(intent)
            finish()
            return
        }

        setContentView(R.layout.activity_language)

        findViewById<Button>(R.id.btnEnglish).setOnClickListener {
            setLanguage("en")
        }

        findViewById<Button>(R.id.btnTamil).setOnClickListener {
            setLanguage("ta")
        }
    }

    private fun setLanguage(langCode: String) {
        prefs.language = langCode
        prefs.isFirstLaunch = false
        startActivity(Intent(this, AuthActivity::class.java))
        finish()
    }
}
