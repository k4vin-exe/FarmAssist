package com.farmassist.app.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.farmassist.app.R
import com.farmassist.app.utils.SharedPreferencesHelper

class AuthActivity : AppCompatActivity() {
    private lateinit var prefs: SharedPreferencesHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        prefs = SharedPreferencesHelper(this)

        if (prefs.userPin != null) {
            startActivity(Intent(this, DashboardActivity::class.java))
            finish()
            return
        }

        setContentView(R.layout.activity_auth)

        val etPin = findViewById<EditText>(R.id.etPin)
        val etLandSize = findViewById<EditText>(R.id.etLandSize)
        val etDistrict = findViewById<EditText>(R.id.etDistrict)
        val btnSave = findViewById<Button>(R.id.btnSaveProfile)

        btnSave.setOnClickListener {
            val pin = etPin.text.toString()
            val landStr = etLandSize.text.toString()
            val dist = etDistrict.text.toString()

            if (pin.length == 4 && landStr.isNotEmpty() && dist.isNotEmpty()) {
                prefs.userPin = pin
                prefs.landSizeAcres = landStr.toFloatOrNull() ?: 1f
                prefs.district = dist
                
                startActivity(Intent(this, DashboardActivity::class.java))
                finish()
            } else {
                Toast.makeText(this, "Please fill all details correctly (4-digit PIN)", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
