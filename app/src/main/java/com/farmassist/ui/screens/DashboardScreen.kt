package com.farmassist.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.farmassist.R
import com.farmassist.ui.theme.*

data class ModuleConfig(
    val nameRes: Int,
    val descRes: Int,
    val route: String,
    val emoji: String,
    val gradientStart: Color,
    val gradientEnd: Color
)

val premiumModules = listOf(
    ModuleConfig(R.string.module_crop_setup,   R.string.module_crop_setup_desc,   "crop_estimation", "🌾", GradientGreenStart, GradientGreenEnd),
    ModuleConfig(R.string.module_cost_yield,   R.string.module_cost_yield_desc,   "cost_yield",      "💰", GradientOrangeStart, GradientOrangeEnd),
    ModuleConfig(R.string.module_maintenance,  R.string.module_maintenance_desc,  "maintenance",     "📅", GradientBlueStart,  GradientBlueEnd),
    ModuleConfig(R.string.module_terrace,      R.string.module_terrace_desc,      "terrace",         "🏡", GradientPurpleStart, GradientPurpleEnd),
    ModuleConfig(R.string.module_waste,        R.string.module_waste_desc,        "waste",           "♻️", GradientTealStart,  GradientTealEnd),
    ModuleConfig(R.string.module_schemes,      R.string.module_schemes_desc,      "schemes",         "🏛️", GradientGreenStart, GradientGreenEnd),
    ModuleConfig(R.string.module_news,         R.string.module_news_desc,         "news",            "📰", GradientOrangeStart, GradientOrangeEnd)
)

@Composable
fun DashboardScreen(
    userName: String,
    isGuest: Boolean,
    onNavigate: (String) -> Unit,
    onSettingsClick: () -> Unit
) {
    val activeModules = if (isGuest)
        premiumModules.filter { it.route != "crop_estimation" && it.route != "maintenance" }
    else premiumModules

    val displayName = if (isGuest) stringResource(R.string.guest) else userName

    Box(modifier = Modifier.fillMaxSize().background(FarmBackground)) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 220.dp, bottom = 24.dp),
            horizontalArrangement = Arrangement.spacedBy(14.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(activeModules) { module ->
                ModuleCard(module = module, onClick = { onNavigate(module.route) })
            }
        }

        // Floating header rendered above grid
        DashboardHeader(
            displayName = displayName,
            moduleCount = activeModules.size,
            onSettingsClick = onSettingsClick,
            isGuest = isGuest
        )
    }
}

@Composable
fun DashboardHeader(displayName: String, moduleCount: Int, onSettingsClick: () -> Unit, isGuest: Boolean) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(210.dp)
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(FarmGreenDark, FarmGreenMid)
                )
            )
    ) {
        // Decorative circles in background
        Box(modifier = Modifier.size(180.dp).offset(x = 240.dp, y = (-40).dp).clip(CircleShape).background(Color.White.copy(alpha = 0.04f)))
        Box(modifier = Modifier.size(120.dp).offset(x = (-30).dp, y = 120.dp).clip(CircleShape).background(Color.White.copy(alpha = 0.04f)))

        Column(
            modifier = Modifier.fillMaxSize().padding(horizontal = 24.dp, vertical = 20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("🌿", fontSize = 14.sp)
                        Spacer(Modifier.width(6.dp))
                        Text(stringResource(R.string.dashboard_farm_assist_caps), color = FarmGreenAccent, fontSize = 11.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.5.sp)
                    }
                    Spacer(Modifier.height(6.dp))
                    Text(
                        text = "${stringResource(R.string.hello)}, $displayName! 👋",
                        color = Color.White,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                // Settings button
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(Color.White.copy(alpha = 0.15f))
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null
                        ) { onSettingsClick() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.Settings, contentDescription = "Settings", tint = Color.White, modifier = Modifier.size(22.dp))
                }
            }

            Spacer(Modifier.height(16.dp))
            Text(
                text = stringResource(R.string.grow_today),
                color = Color.White.copy(alpha = 0.70f),
                fontSize = 14.sp
            )
            Spacer(Modifier.height(16.dp))

            // Stats pills
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                StatPill("$moduleCount", stringResource(R.string.maintenance_schedule).split(" ")[1], FarmGreenAccent) // Reusing schedule word as 'Modules' hack or just use new string
                StatPill("TN", "Tamil Nadu", FarmOrangeAccent)
                if (!isGuest) StatPill("👤", stringResource(R.string.farmer_label), Color.White.copy(alpha = 0.7f))
            }
        }
    }
}

@Composable
fun StatPill(value: String, label: String, valueColor: Color) {
    Surface(
        shape = RoundedCornerShape(20.dp),
        color = Color.White.copy(alpha = 0.12f)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(value, color = valueColor, fontWeight = FontWeight.Bold, fontSize = 13.sp)
            Spacer(Modifier.width(4.dp))
            Text(label, color = Color.White.copy(alpha = 0.75f), fontSize = 11.sp)
        }
    }
}

@Composable
fun ModuleCard(module: ModuleConfig, onClick: () -> Unit) {
    Card(
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        modifier = Modifier
            .fillMaxWidth()
            .height(154.dp)
            .shadow(elevation = 8.dp, shape = RoundedCornerShape(20.dp), ambientColor = module.gradientStart.copy(alpha = 0.3f))
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) { onClick() }
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(module.gradientStart, module.gradientEnd)
                    )
                )
        ) {
            // Background emoji watermark
            Text(
                text = module.emoji,
                fontSize = 64.sp,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .offset(x = 12.dp, y = 12.dp)
                    .alpha(0.15f)
            )

            Column(
                modifier = Modifier.fillMaxSize().padding(16.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Text(module.emoji, fontSize = 28.sp)
                Column {
                    Text(
                        text = stringResource(module.nameRes),
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(
                        text = stringResource(module.descRes),
                        color = Color.White.copy(alpha = 0.80f),
                        fontSize = 11.sp,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        lineHeight = 14.sp
                    )
                }
            }
        }
    }
}
