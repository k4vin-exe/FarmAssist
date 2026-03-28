package com.farmassist.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val FarmLightColorScheme = lightColorScheme(
    primary = FarmGreenPrimary,
    onPrimary = Color.White,
    primaryContainer = FarmGreenLight,
    onPrimaryContainer = FarmTextPrimary,
    secondary = FarmOrangeSecondary,
    onSecondary = Color.White,
    background = FarmBackground,
    onBackground = FarmTextPrimary,
    surface = FarmSurface,
    onSurface = FarmTextSecondary
)

@Composable
fun FarmAssistTheme(content: @Composable () -> Unit) {
    // Custom Material 3 theme applying the Farm concept
    MaterialTheme(
        colorScheme = FarmLightColorScheme,
        content = content
    )
}
