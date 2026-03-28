package com.farmassist.ui.screens

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.farmassist.ui.theme.*
import com.farmassist.ui.viewmodels.InfoViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TerraceFarmingScreen(viewModel: InfoViewModel) {
    val items by viewModel.terraceFarming.collectAsState()
    val myTerraceCrops by viewModel.myTerraceCrops.collectAsState()
    
    var selectedSunlight by remember { mutableStateOf("Full") }
    var expandedSunlight by remember { mutableStateOf(false) }

    val suggestedCrops = items.filter { it.sunlight.equals(selectedSunlight, ignoreCase = true) }

    Column(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
        Box(
            modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp)).background(FarmGreenPrimary).padding(bottom = 24.dp, start = 24.dp, end = 24.dp, top = 16.dp)
        ) {
            Column {
                Text("Terrace Farming", color = Color.White, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(8.dp))
                Text("Optimize your rooftop. Discover high-yield crops perfectly suited for urban container gardening.", color = Color.White.copy(alpha = 0.85f), fontSize = 14.sp)
            }
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            
            // 1. My Planted Terrace Crops
            if (myTerraceCrops.isNotEmpty()) {
                item { Text("Your Active Terrace Farm", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = FarmTextPrimary) }
                items(myTerraceCrops.toList()) { cropName ->
                    val cropRef = items.find { it.crop == cropName }
                    if (cropRef != null) {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(containerColor = FarmGreenLight),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                                    Text(cropName, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = FarmGreenPrimary)
                                    IconButton(onClick = { viewModel.toggleTerraceCrop(cropName, false) }) {
                                        Icon(Icons.Default.Delete, contentDescription = "Remove", tint = Color.Red)
                                    }
                                }
                                Divider(color = Color.White)
                                Spacer(modifier = Modifier.height(8.dp))
                                Row {
                                    Icon(Icons.Default.CheckCircle, contentDescription=null, tint=FarmOrangeSecondary, modifier=Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("Maintenance: Water " + cropRef.water, fontSize = 13.sp)
                                }
                                Spacer(modifier = Modifier.height(6.dp))
                                Row {
                                    Icon(Icons.Default.Check, contentDescription=null, tint=FarmOrangeSecondary, modifier=Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("Harvest Timeline: " + cropRef.days + " Days", fontSize = 13.sp)
                                }
                            }
                        }
                    }
                }
                item { Spacer(modifier = Modifier.height(16.dp)) }
                item { Divider(color = Color.LightGray.copy(alpha=0.5f)) }
                item { Spacer(modifier = Modifier.height(8.dp)) }
            }

            // 2. Assessment Tool
            item {
                Text("Crop Suggester", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = FarmTextPrimary)
                Spacer(modifier = Modifier.height(12.dp))
                ExposedDropdownMenuBox(
                    expanded = expandedSunlight,
                    onExpandedChange = { expandedSunlight = !expandedSunlight },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedTextField(
                        value = selectedSunlight + " Sunlight",
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Available Sunlight") },
                        modifier = Modifier.menuAnchor().fillMaxWidth(),
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedSunlight) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = FarmGreenPrimary,
                            unfocusedBorderColor = Color.Gray
                        ),
                        shape = RoundedCornerShape(12.dp)
                    )
                    ExposedDropdownMenu(
                        expanded = expandedSunlight,
                        onDismissRequest = { expandedSunlight = false }
                    ) {
                        listOf("Full", "Partial", "Shade").forEach { sun ->
                            DropdownMenuItem(text = { Text(sun) }, onClick = { selectedSunlight = sun; expandedSunlight = false })
                        }
                    }
                }
            }

            // 3. Suggested Crops Listing
            item { Spacer(modifier = Modifier.height(8.dp)) }
            if (suggestedCrops.isEmpty()) {
                item { Text("No crops currently match this condition.", color = Color.Gray) }
            }
            items(suggestedCrops) { item ->
                val isPlanted = myTerraceCrops.contains(item.crop)
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color.LightGray),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(imageVector = Icons.Default.Star, contentDescription = null, tint = FarmGreenPrimary, modifier = Modifier.size(24.dp))
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(item.crop, fontSize = 20.sp, fontWeight = FontWeight.Bold, color = FarmTextPrimary)
                        }
                        
                        Spacer(modifier = Modifier.height(16.dp))
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            InfoBadge(icon = Icons.Default.Info, label = item.sunlight, color = FarmOrangeSecondary)
                            InfoBadge(icon = Icons.Default.CheckCircle, label = item.water, color = Color(0xFF1976D2))
                        }
                        Spacer(modifier = Modifier.height(16.dp))

                        Button(
                            onClick = { viewModel.toggleTerraceCrop(item.crop, !isPlanted) },
                            modifier = Modifier.fillMaxWidth().height(48.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (isPlanted) Color(0xFFFFEBEE) else FarmOrangeSecondary,
                                contentColor = if (isPlanted) Color(0xFFD32F2F) else Color.White
                            )
                        ) {
                            Text(text = if (isPlanted) "Remove Crop" else "Plant on Terrace", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun WasteManagementScreen(viewModel: InfoViewModel) {
    val items by viewModel.wastes.collectAsState()
    val terraceCrops by viewModel.myTerraceCrops.collectAsState()
    val fieldCrops by viewModel.myFieldCrops.collectAsState()

    val allActiveCrops = terraceCrops + fieldCrops
    
    val myWasteCrops = items.filter { wasteItem ->
        allActiveCrops.any { activeCrop ->
            wasteItem.waste.contains(activeCrop, ignoreCase = true)
        }
    }
    
    val generalWastes = items - myWasteCrops.toSet()

    Column(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
        Box(
             modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp)).background(FarmGreenPrimary).padding(bottom = 24.dp, start = 24.dp, end = 24.dp, top = 16.dp)
        ) {
            Column {
                Text("Waste Recovery", color = Color.White, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(8.dp))
                Text("Analyze your active crops and transform agricultural byproducts into profitable eco-materials.", color = Color.White.copy(alpha = 0.85f), fontSize = 14.sp)
            }
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            if (myWasteCrops.isNotEmpty()) {
                item { Text("Manage Your Crop Waste", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = FarmTextPrimary) }
                items(myWasteCrops) { item ->
                    WasteCard(item = item, isPriority = true)
                }
                item { Spacer(modifier = Modifier.height(16.dp)) }
                item { Divider(color = Color.LightGray.copy(alpha = 0.5f)) }
                item { Spacer(modifier = Modifier.height(8.dp)) }
            }

            item { Text("General Education Catalog", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = FarmTextPrimary) }
            items(generalWastes) { item ->
                WasteCard(item = item, isPriority = false)
            }
        }
    }
}

@Composable
fun WasteCard(item: com.farmassist.data.local.model.Waste, isPriority: Boolean) {
    Card(
        modifier = Modifier.fillMaxWidth().animateContentSize(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = if (isPriority) FarmGreenLight else Color.White),
        border = androidx.compose.foundation.BorderStroke(1.dp, if (isPriority) FarmGreenPrimary else Color.LightGray),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Refresh, contentDescription = null, tint = FarmGreenPrimary, modifier = Modifier.size(24.dp))
                Spacer(modifier = Modifier.width(12.dp))
                Text(item.waste, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = FarmTextPrimary)
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            Text("Conversion Method: " + item.reuse, fontSize = 15.sp, color = FarmOrangeSecondary, fontWeight = FontWeight.SemiBold)
            
            Spacer(modifier = Modifier.height(16.dp))
            Text("Best Practice Steps:", fontSize = 14.sp, color = FarmTextSecondary, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            
            item.steps.forEachIndexed { index, step ->
                Row(modifier = Modifier.padding(bottom = 6.dp), verticalAlignment = Alignment.Top) {
                    Text((index + 1).toString() + ".", fontSize = 14.sp, color = FarmGreenPrimary, fontWeight = FontWeight.Bold, modifier = Modifier.width(20.dp))
                    Text(step, fontSize = 14.sp, color = FarmTextPrimary, lineHeight = 20.sp)
                }
            }
        }
    }
}

@Composable
fun SchemesScreen(viewModel: InfoViewModel) {
    val items by viewModel.schemes.collectAsState()

    Column(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
        Box(
            modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp)).background(FarmGreenPrimary).padding(bottom = 24.dp, start = 24.dp, end = 24.dp, top = 16.dp)
        ) {
            Column {
                Text("Govt. Initiatives", color = Color.White, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(8.dp))
                Text("Access official agricultural insurance, subsidies, and financial relief programs.", color = Color.White.copy(alpha = 0.85f), fontSize = 14.sp)
            }
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(items) { scheme ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = FarmGreenLight),
                    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Home, contentDescription = null, tint = FarmOrangeSecondary, modifier = Modifier.size(24.dp))
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(scheme.name, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = FarmTextPrimary)
                        }
                        
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        Row(verticalAlignment = Alignment.Top) {
                            Icon(Icons.Default.CheckCircle, contentDescription = null, tint = FarmGreenPrimary, modifier = Modifier.size(18.dp).padding(top=2.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Text("Subsidy / Benefit", fontSize = 12.sp, color = FarmTextSecondary)
                                Text(scheme.benefit, fontSize = 14.sp, color = FarmTextPrimary, fontWeight = FontWeight.Medium, lineHeight = 20.sp)
                            }
                        }
                        
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        Surface(shape = RoundedCornerShape(8.dp), color = Color.White, border = androidx.compose.foundation.BorderStroke(1.dp, Color.LightGray)) {
                            Text("Eligibility: " + scheme.eligibility, fontSize = 13.sp, color = FarmTextSecondary, modifier = Modifier.padding(12.dp), lineHeight = 18.sp)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun InfoBadge(icon: androidx.compose.ui.graphics.vector.ImageVector, label: String, color: Color) {
    Surface(
        shape = RoundedCornerShape(8.dp),
        color = Color.White,
        modifier = Modifier.padding(end = 8.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
        ) {
            Icon(imageVector = icon, contentDescription = null, tint = color, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(6.dp))
            Text(label, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = FarmTextPrimary)
        }
    }
}
