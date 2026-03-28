package com.farmassist.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun LanguageScreen(onLanguageSelected: (String) -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Choose your language / உங்கள் மொழியை தேர்வு செய்யவும்")
        Spacer(modifier = Modifier.height(32.dp))
        Button(onClick = { onLanguageSelected("en") }) {
            Text(text = "English")
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { onLanguageSelected("ta") }) {
            Text(text = "தமிழ் (Tamil)")
        }
    }
}
