package com.farmassist.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.expandVertically
<<<<<<< HEAD
=======
import androidx.compose.animation.animateContentSize
>>>>>>> master
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.farmassist.R
import com.farmassist.ui.theme.*
import com.farmassist.ui.viewmodels.MaintenanceViewModel
import com.farmassist.util.DataTranslator

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MaintenanceScreen(viewModel: MaintenanceViewModel) {
    val crops        by viewModel.crops.collectAsState()
    val schedules    by viewModel.schedules.collectAsState()
    val fertilizers  by viewModel.fertilizers.collectAsState()
    val pests        by viewModel.pests.collectAsState()
    val irrigation   by viewModel.irrigation.collectAsState()
    val smartAdvice  by viewModel.smartAdvice.collectAsState()
    val plantedCrops by viewModel.plantedCrops.collectAsState()

    var selectedCropName by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }

    val isPlanted = selectedCropName.isNotEmpty() && plantedCrops.contains(selectedCropName)

    val titleStr      = stringResource(R.string.maintenance_title)
    val subtitleStr   = stringResource(R.string.maintenance_subtitle)
    val myFarmStr     = stringResource(R.string.maintenance_my_farm)
    val noCropsStr    = stringResource(R.string.maintenance_no_crops)
    val irrigStr      = stringResource(R.string.maintenance_irrigation_title)
    val scheduleStr   = stringResource(R.string.maintenance_schedule)
    val fertStr       = stringResource(R.string.maintenance_fertilizer)
    val dayStr        = stringResource(R.string.maintenance_day)
    val everyStr      = stringResource(R.string.maintenance_irrigate_every)
    val daysStr       = stringResource(R.string.maintenance_days)
    val categoryStr   = stringResource(R.string.maintenance_category)
    val browseAllStr  = stringResource(R.string.maintenance_browse_all)
    val removeBtnStr  = stringResource(R.string.maintenance_remove_btn)
    val plantBtnStr   = stringResource(R.string.maintenance_plant_btn)
    val triggerStr    = stringResource(R.string.maintenance_trigger)

    // Auto-select first planted crop only once
    LaunchedEffect(crops) {
        if (selectedCropName.isEmpty() && crops.isNotEmpty() && plantedCrops.isNotEmpty()) {
            val first = plantedCrops.firstOrNull() ?: return@LaunchedEffect
            if (crops.any { it.crop == first }) {
                selectedCropName = first
                viewModel.loadCropDetails(first)
            }
        }
    }

    Column(modifier = Modifier.fillMaxSize().background(FarmBackground)) {
        // Header
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Brush.verticalGradient(listOf(GradientBlueEnd, GradientBlueStart)))
                .padding(start = 24.dp, end = 24.dp, top = 20.dp, bottom = 28.dp)
        ) {
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("📅", fontSize = 14.sp)
                    Spacer(Modifier.width(6.dp))
                    Text(categoryStr, color = Color.White.copy(alpha = 0.80f), fontSize = 11.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.5.sp)
                }
                Spacer(Modifier.height(8.dp))
                Text(titleStr, color = Color.White, fontSize = 24.sp, fontWeight = FontWeight.ExtraBold)
                Spacer(Modifier.height(4.dp))
                Text(subtitleStr, color = Color.White.copy(alpha = 0.82f), fontSize = 13.sp)
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 18.dp, vertical = 18.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // My Farm chip row
            if (plantedCrops.isNotEmpty()) {
                Text(myFarmStr, fontSize = 15.sp, fontWeight = FontWeight.ExtraBold, color = FarmTextPrimary)
                Row(
                    modifier = Modifier.horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    plantedCrops.forEach { cropName ->
                        val isSel = selectedCropName == cropName
                        Surface(
                            shape = RoundedCornerShape(50),
                            color = if (isSel) GradientBlueStart else FarmSurface,
                            shadowElevation = if (isSel) 4.dp else 1.dp,
                            onClick = {
                                selectedCropName = cropName
                                viewModel.loadCropDetails(cropName)
                            }
                        ) {
                            Row(
                                Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                if (isSel) {
                                    Icon(Icons.Default.CheckCircle, null, tint = Color.White, modifier = Modifier.size(14.dp))
                                    Spacer(Modifier.width(6.dp))
                                }
                                Text(
                                    DataTranslator.translate(cropName), 
                                    color = if (isSel) Color.White else FarmTextPrimary, 
                                    fontWeight = if (isSel) FontWeight.Bold else FontWeight.Normal, 
                                    fontSize = 14.sp
                                )
                            }
                        }
                    }
                }
            }

            // Crop explorer dropdown
            ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = !expanded }) {
                OutlinedTextField(
                    value = DataTranslator.translate(selectedCropName).ifEmpty { "" },
                    onValueChange = {},
                    readOnly = true,
                    label = { Text(browseAllStr, color = FarmTextHint) },
                    modifier = Modifier.menuAnchor().fillMaxWidth(),
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = GradientBlueStart,
                        unfocusedBorderColor = Color(0xFFDDDDDD),
                        focusedLabelColor = GradientBlueStart,
                        focusedTextColor = FarmTextPrimary,
                        unfocusedTextColor = FarmTextPrimary
                    ),
                    shape = RoundedCornerShape(14.dp)
                )
                ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                    crops.forEach { c ->
                        DropdownMenuItem(
                            text = { Text(DataTranslator.translate(c.crop), color = FarmTextPrimary) },
                            onClick = {
                                selectedCropName = c.crop
                                expanded = false
                                viewModel.loadCropDetails(c.crop)
                            }
                        )
                    }
                }
            }

            // Plant / Remove button
            if (selectedCropName.isNotEmpty()) {
                Button(
                    onClick = { viewModel.togglePlantedCrop(selectedCropName, !isPlanted) },
                    modifier = Modifier.fillMaxWidth().height(54.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isPlanted) Color(0xFFFFEBEE) else GradientBlueStart,
                        contentColor = if (isPlanted) StatusDanger else Color.White
                    )
                ) {
                    Icon(
                        if (isPlanted) Icons.Default.Delete else Icons.Default.Add,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(
                        if (isPlanted) removeBtnStr else plantBtnStr,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.ExtraBold
                    )
                }
            }

            // Smart advice banner
            smartAdvice?.let { advice ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (advice.isAlert) Color(0xFFFFF3E0) else FarmGreenLight
                    )
                ) {
                    Row(Modifier.padding(16.dp), verticalAlignment = Alignment.Top) {
                        Box(
                            Modifier.size(44.dp).clip(CircleShape)
                                .background(if (advice.isAlert) StatusWarning.copy(0.15f) else FarmGreenMid.copy(0.15f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                if (advice.isAlert) Icons.Default.Warning else Icons.Default.Info,
                                null,
                                tint = if (advice.isAlert) StatusWarning else FarmGreenMid,
                                modifier = Modifier.size(22.dp)
                            )
                        }
                        Spacer(Modifier.width(14.dp))
                        Column {
                            Text(advice.title, fontWeight = FontWeight.ExtraBold, fontSize = 15.sp, color = if (advice.isAlert) StatusWarning else FarmGreenMid)
                            Spacer(Modifier.height(6.dp))
                            Text(advice.description, fontSize = 13.sp, color = FarmTextSecondary, lineHeight = 19.sp)
                        }
                    }
                }
            }

            // Schedule timeline
            if (schedules.isNotEmpty()) {
                MLabel(scheduleStr, "📋")
                schedules.forEachIndexed { idx, s ->
                    StepCard(
                        number = idx + 1,
                        badge = "$dayStr ${s.day}",
<<<<<<< HEAD
                        detail = DataTranslator.translate(s.activity),
=======
                        stage = s.stage,
                        detail = DataTranslator.translate(s.activity),
                        description = s.description,
>>>>>>> master
                        color = GradientBlueStart
                    )
                }
            }

            // Fertilizer timeline
            if (fertilizers.isNotEmpty()) {
                MLabel(fertStr, "🌿")
                fertilizers.forEachIndexed { idx, f ->
                    StepCard(
                        number = idx + 1,
                        badge = "$dayStr ${f.day}",
<<<<<<< HEAD
                        detail = DataTranslator.translate(f.fertilizer),
=======
                        stage = "Fertilizer",
                        detail = DataTranslator.translate(f.fertilizer),
                        description = f.description,
>>>>>>> master
                        color = FarmOrangeSecondary
                    )
                }
            }

            // Irrigation & pests
            if (irrigation != null || pests.isNotEmpty()) {
                MLabel(irrigStr, "💧")
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = FarmSurface),
                    elevation = CardDefaults.cardElevation(2.dp)
                ) {
                    Column(Modifier.padding(16.dp)) {
                        irrigation?.let { irr ->
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text("💧", fontSize = 20.sp)
                                Spacer(Modifier.width(10.dp))
                                Text(
                                    "$everyStr ${irr.interval_days} $daysStr",
                                    fontWeight = FontWeight.SemiBold,
                                    color = GradientBlueStart
                                )
                            }
                            if (pests.isNotEmpty()) {
                                HorizontalDivider(Modifier.padding(vertical = 12.dp), color = Color(0xFFEEEEEE))
                            }
                        }
                        pests.forEach { p ->
                            Row(
                                Modifier.padding(vertical = 5.dp),
                                verticalAlignment = Alignment.Top
                            ) {
                                Text("🐛", fontSize = 14.sp)
                                Spacer(Modifier.width(8.dp))
                                Column {
                                    Text(DataTranslator.translate(p.risk), fontWeight = FontWeight.Bold, fontSize = 13.sp, color = FarmTextPrimary)
                                    Text("$triggerStr ${DataTranslator.translate(p.condition)}", fontSize = 12.sp, color = FarmTextSecondary)
                                }
                            }
                        }
                    }
                }
            }

            // Empty state when no crop selected
            if (selectedCropName.isEmpty() && plantedCrops.isEmpty()) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = FarmSurface),
                    elevation = CardDefaults.cardElevation(3.dp)
                ) {
                    Column(
                        Modifier.padding(32.dp).fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("🌱", fontSize = 52.sp)
                        Spacer(Modifier.height(14.dp))
                        Text(noCropsStr, color = FarmTextSecondary, textAlign = TextAlign.Center, fontSize = 14.sp, lineHeight = 21.sp)
                    }
                }
            }

            Spacer(Modifier.height(30.dp))
        }
    }
}

@Composable
private fun MLabel(title: String, emoji: String) {
    Spacer(Modifier.height(4.dp))
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(emoji, fontSize = 18.sp)
        Spacer(Modifier.width(8.dp))
        Text(title, fontSize = 15.sp, fontWeight = FontWeight.ExtraBold, color = FarmTextPrimary)
    }
}

@Composable
<<<<<<< HEAD
private fun StepCard(number: Int, badge: String, detail: String, color: Color) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(bottom = 6.dp),
=======
private fun StepCard(number: Int, badge: String, stage: String = "", detail: String, description: String = "", color: Color) {
    var expanded by remember { mutableStateOf(false) }
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 6.dp)
            .animateContentSize()
            .then(if (description.isNotEmpty()) Modifier.clickable { expanded = !expanded } else Modifier),
>>>>>>> master
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = FarmSurface),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
<<<<<<< HEAD
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 14.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Step circle
            Box(
                modifier = Modifier.size(32.dp).clip(CircleShape).background(color),
                contentAlignment = Alignment.Center
            ) {
                Text("$number", color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.Bold)
            }
            Spacer(Modifier.width(12.dp))
            // Day badge
            Surface(shape = RoundedCornerShape(6.dp), color = color.copy(alpha = 0.12f)) {
                Text(
                    badge,
                    color = color,
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                )
            }
            Spacer(Modifier.width(12.dp))
            Text(detail, fontSize = 14.sp, color = FarmTextPrimary, fontWeight = FontWeight.Medium, modifier = Modifier.weight(1f))
=======
        Column(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 14.dp, vertical = 12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Step circle
                Box(
                    modifier = Modifier.size(32.dp).clip(CircleShape).background(color),
                    contentAlignment = Alignment.Center
                ) {
                    Text("$number", color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                }
                Spacer(Modifier.width(12.dp))
                // Day badge
                Surface(shape = RoundedCornerShape(6.dp), color = color.copy(alpha = 0.12f)) {
                    Text(
                        badge,
                        color = color,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                    )
                }
                // Stage badge
                if (stage.isNotEmpty()) {
                    Spacer(Modifier.width(6.dp))
                    Surface(shape = RoundedCornerShape(6.dp), color = Color(0xFFEEEEEE)) {
                        Text(
                            stage,
                            color = FarmTextSecondary,
                            fontWeight = FontWeight.Medium,
                            fontSize = 11.sp,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }
                Spacer(Modifier.weight(1f))
                // Expand indicator
                if (description.isNotEmpty()) {
                    Icon(
                        imageVector = if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                        contentDescription = null,
                        tint = color.copy(alpha = 0.7f),
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
            Spacer(Modifier.height(8.dp))
            Text(detail, fontSize = 14.sp, color = FarmTextPrimary, fontWeight = FontWeight.Medium)
            // Expandable description
            if (description.isNotEmpty() && expanded) {
                Spacer(Modifier.height(10.dp))
                HorizontalDivider(color = color.copy(alpha = 0.15f))
                Spacer(Modifier.height(10.dp))
                Text(
                    description,
                    fontSize = 13.sp,
                    color = FarmTextSecondary,
                    lineHeight = 20.sp
                )
            }
>>>>>>> master
        }
    }
}

// Keep these as public helpers used by other screens
@Composable
fun SectionHeader(title: String, icon: androidx.compose.ui.graphics.vector.ImageVector) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(imageVector = icon, contentDescription = null, tint = FarmTextSecondary, modifier = Modifier.size(18.dp))
        Spacer(Modifier.width(8.dp))
        Text(text = title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = FarmTextPrimary)
    }
    Spacer(Modifier.height(8.dp))
}

@Composable
fun MaintenanceTaskCard(label: String, detail: String, color: Color) {
    Card(
        Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = FarmSurface),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(Modifier.fillMaxWidth().padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Surface(shape = RoundedCornerShape(8.dp), color = color.copy(alpha = 0.15f)) {
                Text(label, Modifier.padding(horizontal = 12.dp, vertical = 6.dp), color = color, fontWeight = FontWeight.Bold, fontSize = 13.sp)
            }
            Spacer(Modifier.width(16.dp))
            Text(detail, fontSize = 15.sp, color = FarmTextPrimary, fontWeight = FontWeight.Medium)
        }
    }
}
