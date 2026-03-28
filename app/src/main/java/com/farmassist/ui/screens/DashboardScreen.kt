package com.farmassist.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.farmassist.ui.theme.*

data class ModuleConfig(
    val name: String, 
    val description: String, 
    val route: String, 
    val gradientStart: Color, 
    val gradientEnd: Color
)

val premiumModules = listOf(
    ModuleConfig("Crop Setup", "AI Estimation based on weather & soil", "crop_estimation", GradientGreenStart, GradientGreenEnd),
    ModuleConfig("Cost & Yield", "Calculate ROI based on land size", "cost_yield", GradientOrangeStart, GradientOrangeEnd),
    ModuleConfig("Maintenance", "Daily crop chore reminders", "maintenance", GradientBlueStart, GradientBlueEnd),
    ModuleConfig("Terrace Farming", "Grow food on your roof", "terrace", GradientPurpleStart, GradientPurpleEnd),
    ModuleConfig("Waste Mgmt", "Reuse agricultural waste", "waste", FarmGreenPrimary, FarmGreenLight),
    ModuleConfig("Govt Schemes", "View available subsidies", "schemes", GradientOrangeStart, GradientOrangeEnd),
    ModuleConfig("Agri News", "Latest state news", "news", GradientBlueStart, GradientBlueEnd)
)

@Composable
fun DashboardScreen(userName: String, isGuest: Boolean, onNavigate: (String) -> Unit) {
    val activeModules = if (isGuest) {
        premiumModules.filter { it.route != "crop_estimation" && it.route != "maintenance" }
    } else {
        premiumModules
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Premium Header
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(bottomStart = 32.dp, bottomEnd = 32.dp))
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(FarmGreenPrimary, GradientGreenStart)
                    )
                )
                .padding(32.dp)
        ) {
            val displayName = if (isGuest) "Guest" else userName
            Column {
                Text(
                    text = "Hello, " + displayName + "! \uD83D\uDC4B",
                    color = Color.White,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "What would you like to grow today?",
                    color = FarmGreenLight,
                    fontSize = 16.sp
                )
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Grid Dashboard
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(activeModules) { module ->
                Card(
                    shape = RoundedCornerShape(20.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(140.dp)
                        .clickable { onNavigate(module.route) }
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                brush = Brush.linearGradient(
                                    colors = listOf(module.gradientStart, module.gradientEnd)
                                )
                            )
                            .padding(16.dp)
                    ) {
                        Column(
                            verticalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxSize()
                        ) {
                            Text(
                                text = module.name,
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp
                            )
                            Text(
                                text = module.description,
                                color = Color.White.copy(alpha = 0.9f),
                                fontSize = 12.sp,
                                lineHeight = 16.sp
                            )
                        }
                    }
                }
            }
        }
    }
}
