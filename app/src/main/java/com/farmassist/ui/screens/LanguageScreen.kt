package com.farmassist.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
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
fun LanguageScreen(onLanguageSelected: (String) -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("🌾", fontSize = 60.sp)
        Spacer(modifier = Modifier.height(16.dp))
        Text("Farm Assist", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = FarmGreenPrimary)
        Spacer(modifier = Modifier.height(8.dp))
        Text(stringResource(R.string.choose_language), fontSize = 14.sp, color = FarmTextSecondary)
        Spacer(modifier = Modifier.height(48.dp))

        listOf("en" to "English", "ta" to "தமிழ் (Tamil)").forEach { (code, label) ->
            Button(
                onClick = { onLanguageSelected(code) },
                modifier = Modifier.fillMaxWidth(0.7f).height(56.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(containerColor = FarmGreenPrimary)
            ) {
                Text(label, fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
