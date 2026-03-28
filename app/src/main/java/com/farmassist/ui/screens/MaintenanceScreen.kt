package com.farmassist.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.farmassist.data.local.model.Crop
import com.farmassist.ui.theme.*
import com.farmassist.ui.viewmodels.MaintenanceViewModel
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MaintenanceScreen(viewModel: MaintenanceViewModel) {
    val crops by viewModel.crops.collectAsState()
    val schedules by viewModel.schedules.collectAsState()
    val fertilizers by viewModel.fertilizers.collectAsState()
    val pests by viewModel.pests.collectAsState()
    val irrigation by viewModel.irrigation.collectAsState()
    val smartAdvice by viewModel.smartAdvice.collectAsState()
    val plantedCrops by viewModel.plantedCrops.collectAsState()
    
    var selectedCrop by remember { mutableStateOf<Crop?>(null) }
    var expanded by remember { mutableStateOf(false) }
    val isPlanted = selectedCrop != null && plantedCrops.contains(selectedCrop!!.crop)

    LaunchedEffect(crops) {
        if (crops.isNotEmpty() && plantedCrops.isNotEmpty() && selectedCrop == null) {
            val autoCropName = plantedCrops.first()
            val autoCrop = crops.find { it.crop == autoCropName }
            if (autoCrop != null) {
                selectedCrop = autoCrop
                viewModel.loadCropDetails(autoCrop.crop)
            }
        }
    }

    Column(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
        // Dynamic Header
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp))
                .background(FarmGreenPrimary)
                .padding(bottom = 24.dp, start = 24.dp, end = 24.dp, top = 16.dp)
        ) {
            Column {
                Text("Crop Maintenance", color = Color.White, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(8.dp))
                Text("Manage multiple crops inside your active portfolio and autonomously monitor their timeline.", color = Color.White.copy(alpha = 0.85f), fontSize = 14.sp)
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
        ) {
            // "My Farm" Global Profile Tracker Segment
            if (plantedCrops.isNotEmpty()) {
                Text("Your Farm Portfolio", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = FarmTextPrimary)
                Spacer(modifier = Modifier.height(12.dp))
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(plantedCrops.toList()) { cropName ->
                        val isSelected = selectedCrop?.crop == cropName
                        Surface(
                            shape = RoundedCornerShape(16.dp),
                            color = if (isSelected) FarmGreenPrimary else FarmGreenLight,
                            onClick = {
                                val cropObj = crops.find { it.crop == cropName }
                                if (cropObj != null) {
                                    selectedCrop = cropObj
                                    viewModel.loadCropDetails(cropName)
                                }
                            }
                        ) {
                            Text(
                                text = cropName,
                                color = if (isSelected) Color.White else FarmGreenPrimary,
                                fontWeight = FontWeight.SemiBold,
                                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(24.dp))
                Divider(color = Color.LightGray.copy(alpha = 0.5f))
                Spacer(modifier = Modifier.height(24.dp))
            }

            // Global Catalog Search
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded },
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = selectedCrop?.crop ?: "Browse Catalog",
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Lookup a Crop", color = FarmTextSecondary) },
                    modifier = Modifier.menuAnchor().fillMaxWidth(),
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = FarmGreenPrimary,
                        unfocusedBorderColor = Color.Gray,
                        focusedLabelColor = FarmGreenPrimary,
                        focusedTextColor = FarmTextPrimary,
                        unfocusedTextColor = FarmTextPrimary
                    ),
                    shape = RoundedCornerShape(12.dp)
                )
                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    crops.forEach { cropObj ->
                        DropdownMenuItem(
                            text = { Text(cropObj.crop, color = Color.Black) },
                            onClick = {
                                selectedCrop = cropObj
                                expanded = false
                                viewModel.loadCropDetails(cropObj.crop)
                            }
                        )
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(24.dp))

            AnimatedVisibility(
                visible = selectedCrop != null,
                enter = fadeIn() + expandVertically()
            ) {
                Column(modifier = Modifier.fillMaxSize()) {
                    Button(
                        onClick = { viewModel.togglePlantedCrop(selectedCrop!!.crop, !isPlanted) },
                        modifier = Modifier.fillMaxWidth().height(56.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isPlanted) Color(0xFFFFEBEE) else FarmOrangeSecondary,
                            contentColor = if (isPlanted) Color(0xFFD32F2F) else Color.White
                        )
                    ) {
                        Icon(
                            imageVector = if (isPlanted) Icons.Default.Delete else Icons.Default.Add,
                            contentDescription = "Farm Action",
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (isPlanted) "Remove from My Farm" else "Plant & Track Maintenance",
                            fontSize = 16.sp, 
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))
                    
                    // Smart Irrigation Dashboard
                    val advice = smartAdvice
                    if(advice != null) {
                        Card(
                            modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = if (advice.isAlert) Color(0xFFFFEBEE) else FarmGreenLight
                            ),
                            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                        ) {
                            Column(modifier = Modifier.padding(20.dp)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = if (advice.isAlert) Icons.Default.Warning else Icons.Default.Info, 
                                        contentDescription = "Alert",
                                        tint = if (advice.isAlert) Color(0xFFD32F2F) else FarmGreenPrimary,
                                        modifier = Modifier.size(24.dp)
                                    )
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Text(
                                        text = advice.title, 
                                        fontWeight = FontWeight.Bold, 
                                        fontSize = 18.sp,
                                        color = if (advice.isAlert) Color(0xFFD32F2F) else FarmGreenPrimary
                                    )
                                }
                                Spacer(modifier = Modifier.height(12.dp))
                                Text(
                                    text = advice.description,
                                    fontSize = 14.sp,
                                    color = FarmTextPrimary,
                                    lineHeight = 20.sp
                                )
                            }
                        }
                    }

                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        contentPadding = PaddingValues(bottom = 80.dp)
                    ) {
                        // Action Timeline
                        if (schedules.isNotEmpty()) {
                            item { SectionHeader(title = "Action Timeline", icon = Icons.Default.CheckCircle) }
                            items(schedules) { schedule ->
                                MaintenanceTaskCard("Day " + schedule.day, schedule.activity, FarmGreenPrimary)
                            }
                        }

                        // Fertilizer Timeline
                        if (fertilizers.isNotEmpty()) {
                            item { Spacer(modifier = Modifier.height(8.dp)) }
                            item { SectionHeader(title = "Fertilizer Schedule", icon = Icons.Default.List) }
                            items(fertilizers) { f ->
                                MaintenanceTaskCard("Day " + f.day, f.fertilizer, FarmOrangeSecondary)
                            }
                        }

                        // Irrigation & Pests
                        item { Spacer(modifier = Modifier.height(8.dp)) }
                        item {
                            if (irrigation != null || pests.isNotEmpty()) {
                                SectionHeader(title = "Care & Defense", icon = Icons.Default.Info)
                                Card(
                                    modifier = Modifier.fillMaxWidth(),
                                    shape = RoundedCornerShape(12.dp),
                                    colors = CardDefaults.cardColors(containerColor = FarmGreenLight),
                                    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                                ) {
                                    Column(modifier = Modifier.padding(16.dp)) {
                                        irrigation?.let {
                                            Text(text = "Watering Strategy: Every " + it.interval_days + " Days", fontWeight = FontWeight.SemiBold, color = FarmGreenPrimary)
                                            Spacer(modifier = Modifier.height(12.dp))
                                        }
                                        if (pests.isNotEmpty()) {
                                            Text(text = "Common Threats:", fontWeight = FontWeight.Bold, color = FarmTextPrimary, fontSize = 14.sp)
                                            Spacer(modifier = Modifier.height(6.dp))
                                            pests.forEach { pest ->
                                                Text(text = "• " + pest.risk + " (Trigger: " + pest.condition + ")", fontSize = 13.sp, color = FarmTextSecondary)
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SectionHeader(title: String, icon: androidx.compose.ui.graphics.vector.ImageVector) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(imageVector = icon, contentDescription = null, tint = FarmTextSecondary, modifier = Modifier.size(18.dp))
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = FarmTextPrimary)
    }
    Spacer(modifier = Modifier.height(8.dp))
}

@Composable
fun MaintenanceTaskCard(label: String, detail: String, color: Color) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = color.copy(alpha = 0.15f)
            ) {
                Text(
                    text = label,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                    color = color,
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Text(text = detail, fontSize = 15.sp, color = FarmTextPrimary, fontWeight = FontWeight.Medium)
        }
    }
}
