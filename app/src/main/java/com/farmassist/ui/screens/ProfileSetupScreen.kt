package com.farmassist.ui.screens

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.farmassist.R
import com.farmassist.ui.theme.*

@Composable
fun PinDot(filled: Boolean) {
    val color by animateColorAsState(
        targetValue = if (filled) FarmGreenPrimary else Color.LightGray,
        label = "pin_dot"
    )
    Box(
        modifier = Modifier
            .size(16.dp)
            .clip(CircleShape)
            .background(color)
    )
}

@Composable
fun NumPadKey(
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    textColor: Color = FarmTextPrimary
) {
    Box(
        modifier = modifier
            .size(72.dp)
            .clip(CircleShape)
            .clickable(onClick = onClick)
            .background(FarmGreenLight),
        contentAlignment = Alignment.Center
    ) {
        Text(label, fontSize = 24.sp, fontWeight = FontWeight.Bold, color = textColor)
    }
}

@Composable
fun PinNumPad(
    pinLength: Int,
    onDigit: (String) -> Unit,
    onClear: () -> Unit,
    onDelete: () -> Unit
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        // PIN dots indicator
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            repeat(4) { index -> PinDot(filled = index < pinLength) }
        }

        Spacer(modifier = Modifier.height(32.dp))

        val rows = listOf(
            listOf("1", "2", "3"),
            listOf("4", "5", "6"),
            listOf("7", "8", "9"),
        )

        rows.forEach { row ->
            Row(horizontalArrangement = Arrangement.spacedBy(24.dp)) {
                row.forEach { digit ->
                    NumPadKey(label = digit, onClick = { if (pinLength < 4) onDigit(digit) })
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }

        // Bottom row: Clear | 0 | Backspace
        Row(horizontalArrangement = Arrangement.spacedBy(24.dp), verticalAlignment = Alignment.CenterVertically) {
            NumPadKey(label = "C", onClick = onClear, textColor = Color(0xFFD32F2F))
            NumPadKey(label = "0", onClick = { if (pinLength < 4) onDigit("0") })
            NumPadKey(label = "⌫", onClick = onDelete, textColor = FarmOrangeSecondary)
        }
    }
}

@Composable
fun PinLoginScreen(savedPin: String, onLoginSuccess: () -> Unit) {
    var enteredPin by remember { mutableStateOf("") }
    var errorMsg by remember { mutableStateOf("") }

    LaunchedEffect(enteredPin) {
        if (enteredPin.length == 4) {
            if (enteredPin == savedPin) {
                onLoginSuccess()
            } else {
                errorMsg = "Incorrect PIN. Try again."
                enteredPin = ""
            }
        }
    }

    Column(
        modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background).padding(32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier.size(80.dp).clip(CircleShape).background(FarmGreenLight),
            contentAlignment = Alignment.Center
        ) {
            Text("🌾", fontSize = 36.sp)
        }

        Spacer(modifier = Modifier.height(24.dp))
        Text(stringResource(R.string.enter_pin), fontSize = 22.sp, fontWeight = FontWeight.Bold, color = FarmTextPrimary)
        Spacer(modifier = Modifier.height(8.dp))

        if (errorMsg.isNotEmpty()) {
            Text(errorMsg, color = Color.Red, fontSize = 14.sp, fontWeight = FontWeight.Medium)
            Spacer(modifier = Modifier.height(8.dp))
        }

        Spacer(modifier = Modifier.height(24.dp))

        PinNumPad(
            pinLength = enteredPin.length,
            onDigit = { enteredPin += it },
            onClear = { enteredPin = ""; errorMsg = "" },
            onDelete = { if (enteredPin.isNotEmpty()) enteredPin = enteredPin.dropLast(1) }
        )
    }
}

@Composable
fun ProfileSetupScreen(onProfileSaved: (String, String) -> Unit) {
    var name by remember { mutableStateOf("") }
    var pin by remember { mutableStateOf("") }
    var confirmPin by remember { mutableStateOf("") }
    var step by remember { mutableStateOf(0) } // 0=Name, 1=Set PIN, 2=Confirm PIN
    var error by remember { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background).padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier.size(80.dp).clip(CircleShape).background(FarmGreenLight),
            contentAlignment = Alignment.Center
        ) {
            Text("👨‍🌾", fontSize = 36.sp)
        }

        Spacer(modifier = Modifier.height(24.dp))
        Text(stringResource(R.string.setup_profile), fontSize = 22.sp, fontWeight = FontWeight.Bold, color = FarmTextPrimary)
        Spacer(modifier = Modifier.height(24.dp))

        if (step == 0) {
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text(stringResource(R.string.name_hint)) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = FarmGreenPrimary)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = {
                    if (name.isBlank()) error = "Please enter your name."
                    else { step = 1; error = "" }
                },
                modifier = Modifier.fillMaxWidth().height(52.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = FarmGreenPrimary)
            ) {
                Text("Next", fontWeight = FontWeight.Bold)
            }
        } else {
        val titleText = if (step == 1) stringResource(R.string.pin_hint) else stringResource(R.string.confirm_pin)
        val currentPin = if (step == 1) pin else confirmPin
        val pinMismatchError = stringResource(R.string.error_pin_mismatch)
        val saveProfileLabel = stringResource(R.string.save_profile)

            Text(titleText, fontSize = 16.sp, color = FarmTextSecondary)
            Spacer(modifier = Modifier.height(24.dp))

            PinNumPad(
                pinLength = currentPin.length,
                onDigit = { digit ->
                    if (step == 1) pin += digit else confirmPin += digit
                },
                onClear = { if (step == 1) pin = "" else confirmPin = "" },
                onDelete = {
                    if (step == 1) { if (pin.isNotEmpty()) pin = pin.dropLast(1) }
                    else { if (confirmPin.isNotEmpty()) confirmPin = confirmPin.dropLast(1) }
                }
            )

            Spacer(modifier = Modifier.height(24.dp))

            if (error.isNotEmpty()) {
                Text(error, color = Color.Red, fontSize = 13.sp)
                Spacer(modifier = Modifier.height(8.dp))
            }

            if (step == 1 && pin.length == 4) {
                Button(
                    onClick = { step = 2 },
                    modifier = Modifier.fillMaxWidth().height(52.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = FarmGreenPrimary)
                ) {
                    Text("Next", fontWeight = FontWeight.Bold)
                }
            } else if (step == 2 && confirmPin.length == 4) {
                Button(
                    onClick = {
                        if (pin != confirmPin) {
                            error = pinMismatchError
                            confirmPin = ""
                        } else {
                            onProfileSaved(name, pin)
                        }
                    },
                    modifier = Modifier.fillMaxWidth().height(52.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = FarmGreenPrimary)
                ) {
                    Text(saveProfileLabel, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
