package com.farmassist.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import com.farmassist.R
import com.farmassist.data.local.model.Crop
import com.farmassist.ui.theme.*
import com.farmassist.ui.viewmodels.CropEstimationState
import com.farmassist.ui.viewmodels.CropViewModel
import com.farmassist.util.DataTranslator

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CropEstimationScreen(viewModel: CropViewModel) {
    val uiState      by viewModel.uiState.collectAsState()
    val allDistricts by viewModel.allDistricts.collectAsState()
    val context      = LocalContext.current

    var expanded        by remember { mutableStateOf(false) }
    var selectedDistrict by remember { mutableStateOf("") }

    val titleStr          = stringResource(R.string.crop_title)
    val detectingStr      = stringResource(R.string.crop_detecting)
    val soilStr           = stringResource(R.string.crop_soil)
    val tempStr           = stringResource(R.string.crop_temp)
    val seasonStr         = stringResource(R.string.crop_season)
    val recommendationsStr = stringResource(R.string.crop_recommendations)
    val selectDistrictStr = stringResource(R.string.select_district)
    val loadingDistrictsStr = stringResource(R.string.loading_districts)
    val autoGpsStr        = stringResource(R.string.auto_gps)
    val analysingStr      = stringResource(R.string.crop_analysing)
    val offlineStr        = stringResource(R.string.crop_offline_notice)

    val permLauncher = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
        if (granted) viewModel.estimateCropAuto() else viewModel.setError(context.getString(R.string.crop_error_location))
    }

    // Re-run auto detection once districts are loaded (avoids empty-list race condition)
    LaunchedEffect(allDistricts) {
        if (allDistricts.isEmpty()) return@LaunchedEffect
        if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            viewModel.estimateCropAuto()
        } else {
            // viewModel.setError(...) removed to avoid noise if user just opened the screen
        }
    }

    Column(modifier = Modifier.fillMaxSize().background(FarmBackground)) {
        // Header
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Brush.verticalGradient(listOf(FarmGreenDark, FarmGreenMid)))
                .padding(start = 24.dp, end = 24.dp, top = 20.dp, bottom = 24.dp)
        ) {
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("🌾", fontSize = 14.sp)
                    Spacer(Modifier.width(6.dp))
                    Text("AI CROP ADVISOR", color = FarmGreenAccent, fontSize = 11.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.5.sp)
                }
                Spacer(Modifier.height(8.dp))
                Text(titleStr, color = Color.White, fontSize = 24.sp, fontWeight = FontWeight.ExtraBold)
                Spacer(Modifier.height(16.dp))

                // GPS + District row
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp), verticalAlignment = Alignment.CenterVertically) {
                    Button(
                        onClick = {
                            if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
                                viewModel.estimateCropAuto()
                            else permLauncher.launch(android.Manifest.permission.ACCESS_FINE_LOCATION)
                        },
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = FarmOrangeSecondary)
                    ) {
                        Icon(Icons.Default.Place, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(Modifier.width(6.dp))
                        Text(autoGpsStr, fontWeight = FontWeight.Bold)
                    }

                    ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = !expanded }, modifier = Modifier.weight(1f)) {
                        OutlinedTextField(
                            value = if (selectedDistrict.isEmpty()) selectDistrictStr else DataTranslator.translate(selectedDistrict),
                            onValueChange = {},
                            readOnly = true,
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                            colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = Color.White, unfocusedBorderColor = Color.White.copy(alpha = 0.5f), focusedTextColor = Color.White, unfocusedTextColor = Color.White.copy(alpha = 0.8f)),
                            textStyle = LocalTextStyle.current.copy(fontSize = 13.sp),
                            modifier = Modifier.menuAnchor().fillMaxWidth()
                        )
                        ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                            if (allDistricts.isEmpty()) {
                                DropdownMenuItem(text = { Text(loadingDistrictsStr) }, onClick = {})
                            } else {
                                allDistricts.forEach { d ->
                                    DropdownMenuItem(
                                        text = { Text(DataTranslator.translate(d.district), color = FarmTextPrimary) },
                                        onClick = { selectedDistrict = d.district; expanded = false; viewModel.estimateCropManual(d.district) }
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        when (val state = uiState) {
            is CropEstimationState.Idle -> {
                Box(Modifier.fillMaxSize().padding(40.dp), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("🌾", fontSize = 56.sp)
                        Spacer(Modifier.height(16.dp))
                        Text(detectingStr, color = FarmTextSecondary, textAlign = TextAlign.Center, fontSize = 15.sp)
                    }
                }
            }
            is CropEstimationState.Loading -> {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        CircularProgressIndicator(color = FarmGreenMid, modifier = Modifier.size(52.dp), strokeWidth = 4.dp)
                        Spacer(Modifier.height(16.dp))
                        Text(analysingStr, color = FarmTextSecondary, fontSize = 14.sp)
                    }
                }
            }
            is CropEstimationState.Error -> {
                Box(Modifier.fillMaxSize().padding(40.dp), contentAlignment = Alignment.Center) {
                    Card(shape = RoundedCornerShape(20.dp), colors = CardDefaults.cardColors(containerColor = FarmSurface), elevation = CardDefaults.cardElevation(4.dp)) {
                        Column(Modifier.padding(28.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("🌦️", fontSize = 48.sp)
                            Spacer(Modifier.height(12.dp))
                            Text(state.message.replace("Error: ", ""), color = FarmTextSecondary, textAlign = TextAlign.Center, fontSize = 14.sp, lineHeight = 20.sp)
                        }
                    }
                }
            }
            is CropEstimationState.Success -> {
                LazyColumn(Modifier.fillMaxSize(), contentPadding = PaddingValues(18.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
                    item {
                        // Conditions card
                        Card(shape = RoundedCornerShape(18.dp), colors = CardDefaults.cardColors(containerColor = FarmSurface), elevation = CardDefaults.cardElevation(3.dp), modifier = Modifier.fillMaxWidth()) {
                            Column(Modifier.padding(18.dp)) {
                                if (state.defaultUsed) {
                                    Surface(shape = RoundedCornerShape(6.dp), color = FarmOrangeSoft) {
                                        Row(Modifier.padding(horizontal = 10.dp, vertical = 6.dp), verticalAlignment = Alignment.CenterVertically) {
                                            Icon(Icons.Default.Info, contentDescription = null, tint = FarmOrangeSecondary, modifier = Modifier.size(14.dp))
                                            Spacer(Modifier.width(6.dp))
                                            Text(offlineStr, fontSize = 11.sp, color = FarmOrangeSecondary, fontWeight = FontWeight.Medium)
                                        }
                                    }
                                    Spacer(Modifier.height(12.dp))
                                }
                                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                                    ConditionChip("🏔️", soilStr, DataTranslator.translate(state.soil), FarmGreenMid)
                                    ConditionChip("🌡️", tempStr, "${state.temp}°C", StatusDanger)
                                    ConditionChip("🌿", seasonStr, DataTranslator.translate(state.season), StatusInfo)
                                }
                            }
                        }
                    }
                    item {
                        Text("$recommendationsStr (${state.recommendations.size})", fontSize = 16.sp, fontWeight = FontWeight.ExtraBold, color = FarmTextPrimary)
                    }
                    items(state.recommendations) { crop -> CropResultCard(crop = crop) }
                    item { Spacer(Modifier.height(20.dp)) }
                }
            }
        }
    }
}

@Composable
fun ConditionChip(emoji: String, label: String, value: String, accentColor: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Surface(shape = CircleShape, color = accentColor.copy(alpha = 0.12f)) {
            Text(emoji, fontSize = 20.sp, modifier = Modifier.padding(10.dp))
        }
        Spacer(Modifier.height(6.dp))
        Text(label, fontSize = 11.sp, color = FarmTextHint)
        Text(value, fontSize = 14.sp, fontWeight = FontWeight.ExtraBold, color = accentColor)
    }
}

@Composable
fun ConditionBadge(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(label, fontSize = 12.sp, color = FarmTextSecondary)
        Spacer(Modifier.height(4.dp))
        Text(value, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = FarmGreenMid)
    }
}

@Composable
fun CropResultCard(crop: Crop) {
    Card(
        shape = RoundedCornerShape(18.dp),
        elevation = CardDefaults.cardElevation(3.dp),
        colors = CardDefaults.cardColors(containerColor = FarmSurface),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column {
            // Top gradient band
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Brush.linearGradient(listOf(FarmGreenDark, FarmGreenMid)))
                    .padding(horizontal = 18.dp, vertical = 14.dp)
            ) {
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Text(DataTranslator.translate(crop.crop), fontSize = 20.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)
                    Surface(shape = RoundedCornerShape(50), color = Color.White.copy(alpha = 0.20f)) {
                        Text("${crop.growing_days}d", modifier = Modifier.padding(horizontal = 12.dp, vertical = 5.dp), color = Color.White, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    }
                }
            }
            // Stats row
            Row(Modifier.fillMaxWidth().padding(18.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                CropStat("💸", stringResource(R.string.crop_cost_acre), "₹${crop.cost_per_acre}")
                CropStat("🌾", stringResource(R.string.crop_yield_acre), "${crop.yield_per_acre} kg")
                CropStat("🌡️", stringResource(R.string.crop_temp_range), "${crop.temp_min}–${crop.temp_max}°C")
            }
            // Profitability bar
            val profit = (crop.yield_per_acre * 25 - crop.cost_per_acre).toFloat()
            val maxProfit = 50000f
            Padding(horizontal = 18.dp, bottom = 16.dp) {
                LinearProgressIndicator(
                    progress = { (profit / maxProfit).coerceIn(0f, 1f) },
                    modifier = Modifier.fillMaxWidth().height(8.dp).clip(RoundedCornerShape(4.dp)),
                    color = FarmGreenAccent,
                    trackColor = Color(0xFFEEEEEE)
                )
                Spacer(Modifier.height(4.dp))
                Text(stringResource(R.string.crop_est_profit), fontSize = 11.sp, color = FarmTextHint)
            }
        }
    }
}

@Composable
fun CropStat(emoji: String, label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(emoji, fontSize = 18.sp)
        Spacer(Modifier.height(4.dp))
        Text(label, fontSize = 10.sp, color = FarmTextHint)
        Text(value, fontSize = 13.sp, fontWeight = FontWeight.ExtraBold, color = FarmTextPrimary)
    }
}

@Composable
fun Padding(horizontal: androidx.compose.ui.unit.Dp = 0.dp, bottom: androidx.compose.ui.unit.Dp = 0.dp, content: @Composable ColumnScope.() -> Unit) {
    Column(modifier = Modifier.fillMaxWidth().padding(horizontal = horizontal, vertical = 0.dp).padding(bottom = bottom), content = content)
}
