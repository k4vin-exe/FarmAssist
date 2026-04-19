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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.farmassist.R
import com.farmassist.ui.theme.*
import com.farmassist.ui.viewmodels.InfoViewModel
import com.farmassist.util.DataTranslator

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TerraceFarmingScreen(viewModel: InfoViewModel) {
    val items by viewModel.terraceFarming.collectAsState()
    val myTerraceCrops by viewModel.myTerraceCrops.collectAsState()

    var selectedSunlight by remember { mutableStateOf("Full") }
    var expandedSunlight by remember { mutableStateOf(false) }

    val suggestedCrops = items.filter { it.sunlight.equals(selectedSunlight, ignoreCase = true) }

    val terraceTitle = stringResource(R.string.terrace_title)
    val terraceSubtitle = stringResource(R.string.terrace_subtitle)
    val yourFarmLabel = stringResource(R.string.terrace_your_farm)
    val maintenanceLabel = stringResource(R.string.terrace_maintenance_label)
    val harvestLabel = stringResource(R.string.terrace_harvest_label)
    val harvestDaysLabel = stringResource(R.string.terrace_harvest_days)
    val suggesterLabel = stringResource(R.string.terrace_divider)
    val sunlightLabel = stringResource(R.string.terrace_sunlight_label)
    val noMatchLabel = stringResource(R.string.terrace_no_match)
    val plantBtnLabel = stringResource(R.string.terrace_plant_btn)
    val removeBtnLabel = stringResource(R.string.terrace_remove_btn)

    Column(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
        Box(
            modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp)).background(FarmGreenPrimary).padding(bottom = 24.dp, start = 24.dp, end = 24.dp, top = 16.dp)
        ) {
            Column {
                Text(terraceTitle, color = Color.White, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(8.dp))
                Text(terraceSubtitle, color = Color.White.copy(alpha = 0.85f), fontSize = 14.sp)
            }
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            if (myTerraceCrops.isNotEmpty()) {
                item { Text(yourFarmLabel, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = FarmTextPrimary) }
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
                                    Text(DataTranslator.translate(cropName), fontSize = 18.sp, fontWeight = FontWeight.Bold, color = FarmGreenPrimary)
                                    IconButton(onClick = { viewModel.toggleTerraceCrop(cropName, false) }) {
                                        Icon(Icons.Default.Delete, contentDescription = null, tint = Color.Red)
                                    }
                                }
                                HorizontalDivider(color = Color.White)
                                Spacer(modifier = Modifier.height(8.dp))
                                Row {
                                    Icon(Icons.Default.CheckCircle, contentDescription = null, tint = FarmOrangeSecondary, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("$maintenanceLabel ${DataTranslator.translate(cropRef.water)}", fontSize = 13.sp)
                                }
                                Spacer(modifier = Modifier.height(6.dp))
                                Row {
                                    Icon(Icons.Default.Check, contentDescription = null, tint = FarmOrangeSecondary, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("$harvestLabel: ${cropRef.days} $harvestDaysLabel", fontSize = 13.sp)
                                }
                            }
                        }
                    }
                }
                item { Spacer(modifier = Modifier.height(16.dp)) }
                item { HorizontalDivider(color = Color.LightGray.copy(alpha = 0.5f)) }
                item { Spacer(modifier = Modifier.height(8.dp)) }
            }

            item {
                Text(suggesterLabel, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = FarmTextPrimary)
                Spacer(modifier = Modifier.height(12.dp))
                ExposedDropdownMenuBox(
                    expanded = expandedSunlight,
                    onExpandedChange = { expandedSunlight = !expandedSunlight },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedTextField(
                        value = "${DataTranslator.translate(selectedSunlight)} $sunlightLabel",
                        onValueChange = {},
                        readOnly = true,
                        label = { Text(sunlightLabel) },
                        modifier = Modifier.menuAnchor().fillMaxWidth(),
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedSunlight) },
                        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = FarmGreenPrimary, unfocusedBorderColor = Color.Gray),
                        shape = RoundedCornerShape(12.dp)
                    )
                    ExposedDropdownMenu(expanded = expandedSunlight, onDismissRequest = { expandedSunlight = false }) {
                        listOf("Full", "Partial", "Shade").forEach { sun ->
                            DropdownMenuItem(
                                text = { Text(DataTranslator.translate(sun)) }, 
                                onClick = { selectedSunlight = sun; expandedSunlight = false }
                            )
                        }
                    }
                }
            }

            item { Spacer(modifier = Modifier.height(8.dp)) }
            if (suggestedCrops.isEmpty()) {
                item { Text(noMatchLabel, color = Color.Gray) }
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
                            Text(DataTranslator.translate(item.crop), fontSize = 20.sp, fontWeight = FontWeight.Bold, color = FarmTextPrimary)
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            InfoBadge(icon = Icons.Default.Info, label = DataTranslator.translate(item.sunlight), color = FarmOrangeSecondary)
                            InfoBadge(icon = Icons.Default.CheckCircle, label = DataTranslator.translate(item.water), color = Color(0xFF1976D2))
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
                            Text(text = if (isPlanted) removeBtnLabel else plantBtnLabel, fontWeight = FontWeight.Bold)
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
        allActiveCrops.any { activeCrop -> wasteItem.waste.contains(activeCrop, ignoreCase = true) }
    }
    val generalWastes = items - myWasteCrops.toSet()

    val wasteTitle = stringResource(R.string.waste_title)
    val wasteSubtitle = stringResource(R.string.waste_subtitle)
    val myCropsLabel = stringResource(R.string.waste_my_crops)
    val generalLabel = stringResource(R.string.waste_general)

    Column(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
        Box(modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp)).background(FarmGreenPrimary).padding(bottom = 24.dp, start = 24.dp, end = 24.dp, top = 16.dp)) {
            Column {
                Text(wasteTitle, color = Color.White, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(8.dp))
                Text(wasteSubtitle, color = Color.White.copy(alpha = 0.85f), fontSize = 14.sp)
            }
        }

        LazyColumn(modifier = Modifier.fillMaxSize().padding(24.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
            if (myWasteCrops.isNotEmpty()) {
                item { Text(myCropsLabel, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = FarmTextPrimary) }
                items(myWasteCrops) { item -> WasteCard(item = item, isPriority = true) }
                item { Spacer(modifier = Modifier.height(16.dp)) }
                item { HorizontalDivider(color = Color.LightGray.copy(alpha = 0.5f)) }
                item { Spacer(modifier = Modifier.height(8.dp)) }
            }
            item { Text(generalLabel, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = FarmTextPrimary) }
            items(generalWastes) { item -> WasteCard(item = item, isPriority = false) }
        }
    }
}

@Composable
fun WasteCard(item: com.farmassist.data.local.model.Waste, isPriority: Boolean) {
    val conversionLabel = stringResource(R.string.waste_conversion)
    val stepsLabel = stringResource(R.string.waste_steps)
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
                Text(DataTranslator.translate(item.waste), fontSize = 18.sp, fontWeight = FontWeight.Bold, color = FarmTextPrimary)
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text("$conversionLabel: ${DataTranslator.translate(item.reuse)}", fontSize = 15.sp, color = FarmOrangeSecondary, fontWeight = FontWeight.SemiBold)
            Spacer(modifier = Modifier.height(16.dp))
            Text("$stepsLabel:", fontSize = 14.sp, color = FarmTextSecondary, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            item.steps.forEachIndexed { index, step ->
                Row(modifier = Modifier.padding(bottom = 6.dp), verticalAlignment = Alignment.Top) {
                    Text("${index + 1}.", fontSize = 14.sp, color = FarmGreenPrimary, fontWeight = FontWeight.Bold, modifier = Modifier.width(20.dp))
                    Text(DataTranslator.translate(step), fontSize = 14.sp, color = FarmTextPrimary, lineHeight = 20.sp)
                }
            }
        }
    }
}


@Composable
fun InfoBadge(icon: androidx.compose.ui.graphics.vector.ImageVector, label: String, color: Color) {
    Surface(shape = RoundedCornerShape(8.dp), color = Color.White, modifier = Modifier.padding(end = 8.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)) {
            Icon(imageVector = icon, contentDescription = null, tint = color, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(6.dp))
            Text(label, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = FarmTextPrimary)
        }
    }
}
