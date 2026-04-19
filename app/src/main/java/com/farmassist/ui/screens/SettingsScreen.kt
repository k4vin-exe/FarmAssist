package com.farmassist.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.farmassist.MainActivity
import com.farmassist.R
import com.farmassist.ui.theme.*
import com.farmassist.util.SessionManager

@Composable
fun SettingsScreen(sessionManager: SessionManager) {
    val context = LocalContext.current

    val currentLang = sessionManager.getLanguage()
    var selectedLang by remember { mutableStateOf(currentLang) }

    var newPin by remember { mutableStateOf("") }
    var confirmNewPin by remember { mutableStateOf("") }
    var pinStep by remember { mutableStateOf(0) } // 0=set new, 1=confirm
    var pinMessage by remember { mutableStateOf("") }
    var pinSuccess by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
        // Header
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp))
                .background(FarmGreenPrimary)
                .padding(bottom = 24.dp, start = 24.dp, end = 24.dp, top = 16.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Settings, contentDescription = null, tint = Color.White, modifier = Modifier.size(28.dp))
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(stringResource(R.string.settings_title), color = Color.White, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                    Text("Manage your preferences", color = Color.White.copy(alpha = 0.85f), fontSize = 13.sp)
                }
            }
        }

        Column(modifier = Modifier.padding(24.dp), verticalArrangement = Arrangement.spacedBy(20.dp)) {

            // --- Language Section ---
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = FarmGreenLight),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(stringResource(R.string.settings_language_title), fontSize = 18.sp, fontWeight = FontWeight.Bold, color = FarmTextPrimary)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(stringResource(R.string.settings_language_desc), fontSize = 13.sp, color = FarmTextSecondary)
                    Spacer(modifier = Modifier.height(16.dp))

                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        listOf("en" to "English", "ta" to "தமிழ்").forEach { (code, label) ->
                            val isSelected = selectedLang == code
                            Button(
                                onClick = {
                                    selectedLang = code
                                    sessionManager.saveLanguage(code)
                                    // Full stack restart so attachBaseContext re-applies the new locale everywhere
                                    MainActivity.restartWithNewLocale(context)
                                },
                                shape = RoundedCornerShape(10.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (isSelected) FarmGreenPrimary else Color.White,
                                    contentColor = if (isSelected) Color.White else FarmTextPrimary
                                ),
                                modifier = Modifier.weight(1f)
                            ) {
                                if (isSelected) {
                                    Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                }
                                Text(label, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }

            // --- Reset PIN Section ---
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color.LightGray),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Lock, contentDescription = null, tint = FarmOrangeSecondary, modifier = Modifier.size(22.dp))
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(stringResource(R.string.settings_pin_title), fontSize = 18.sp, fontWeight = FontWeight.Bold, color = FarmTextPrimary)
                            Text(stringResource(R.string.settings_pin_desc), fontSize = 13.sp, color = FarmTextSecondary)
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    if (pinSuccess) {
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = Color(0xFFE8F5E9)
                        ) {
                            Text(
                                stringResource(R.string.settings_pin_updated),
                                color = FarmGreenPrimary,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(12.dp)
                            )
                        }
                    } else {
                        val pinTitle = if (pinStep == 0) stringResource(R.string.settings_new_pin) else stringResource(R.string.settings_confirm_new_pin)
                        val currentPin = if (pinStep == 0) newPin else confirmNewPin

                        Text(pinTitle, fontSize = 14.sp, color = FarmTextSecondary, fontWeight = FontWeight.Medium)
                        Spacer(modifier = Modifier.height(12.dp))

                        PinNumPad(
                            pinLength = currentPin.length,
                            onDigit = { digit ->
                                if (pinStep == 0) newPin += digit else confirmNewPin += digit
                            },
                            onClear = {
                                if (pinStep == 0) newPin = "" else confirmNewPin = ""
                                pinMessage = ""
                            },
                            onDelete = {
                                if (pinStep == 0) { if (newPin.isNotEmpty()) newPin = newPin.dropLast(1) }
                                else { if (confirmNewPin.isNotEmpty()) confirmNewPin = confirmNewPin.dropLast(1) }
                            }
                        )

                        if (pinMessage.isNotEmpty()) {
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(pinMessage, color = Color.Red, fontSize = 13.sp)
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        if (pinStep == 0 && newPin.length == 4) {
                            Button(
                                onClick = { pinStep = 1 },
                                modifier = Modifier.fillMaxWidth().height(48.dp),
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = FarmOrangeSecondary)
                            ) {
                                Text("Next", fontWeight = FontWeight.Bold)
                            }
                        } else if (pinStep == 1 && confirmNewPin.length == 4) {
                            Button(
                                onClick = {
                                    if (newPin != confirmNewPin) {
                                        pinMessage = "PINs do not match."
                                        confirmNewPin = ""
                                        pinStep = 0
                                        newPin = ""
                                    } else {
                                        sessionManager.saveProfile(sessionManager.getUserName(), newPin)
                                        pinSuccess = true
                                    }
                                },
                                modifier = Modifier.fillMaxWidth().height(48.dp),
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = FarmOrangeSecondary)
                            ) {
                                Text(stringResource(R.string.settings_pin_update_btn), fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }
    }
}
