package com.farmassist.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.farmassist.R
import com.farmassist.data.local.model.Crop
import com.farmassist.ui.theme.*
import com.farmassist.ui.viewmodels.CostYieldViewModel
import com.farmassist.util.DataTranslator

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CostYieldScreen(viewModel: CostYieldViewModel) {
    val crops by viewModel.crops.collectAsState()
    var selectedCrop by remember { mutableStateOf<Crop?>(null) }
    var expanded by remember { mutableStateOf(false) }
    var landSize by remember { mutableStateOf("") }
    var resultCost by remember { mutableStateOf(0.0) }
    var resultYield by remember { mutableStateOf(0.0) }
    var resultRevenue by remember { mutableStateOf(0.0) }
    var showResults by remember { mutableStateOf(false) }

    val titleStr        = stringResource(R.string.cost_title)
    val subtitleStr     = stringResource(R.string.cost_subtitle)
    val selectCropStr   = stringResource(R.string.cost_select_crop)
    val landSizeStr     = stringResource(R.string.cost_land_size)
    val calculateStr    = stringResource(R.string.cost_calculate)
    val resultsStr      = stringResource(R.string.cost_results)
    val totalCostStr    = stringResource(R.string.cost_total_cost)
    val expectedYieldStr= stringResource(R.string.cost_expected_yield)
    val revenueStr      = stringResource(R.string.cost_revenue)
    val profitStr       = stringResource(R.string.cost_profit)

    Column(modifier = Modifier.fillMaxSize().background(FarmBackground)) {
        // Premium gradient header
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Brush.verticalGradient(listOf(GradientOrangeEnd, GradientOrangeStart)))
                .padding(start = 24.dp, end = 24.dp, top = 20.dp, bottom = 28.dp)
        ) {
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("💰", fontSize = 14.sp)
                    Spacer(Modifier.width(6.dp))
                    Text(stringResource(R.string.cost_calculator_caps), color = Color.White.copy(alpha = 0.80f), fontSize = 11.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.5.sp)
                }
                Spacer(Modifier.height(8.dp))
                Text(titleStr, color = Color.White, fontSize = 24.sp, fontWeight = FontWeight.ExtraBold)
                Spacer(Modifier.height(4.dp))
                Text(subtitleStr, color = Color.White.copy(alpha = 0.82f), fontSize = 13.sp, lineHeight = 18.sp)
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(Modifier.height(24.dp))

            // Crop selector card
            PremiumSectionCard {
                Text(stringResource(R.string.cost_step_1, selectCropStr), fontSize = 13.sp, color = FarmTextSecondary, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(12.dp))
                ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = !expanded }) {
                    OutlinedTextField(
                        value = DataTranslator.translate(selectedCrop?.crop),
                        onValueChange = {},
                        readOnly = true,
                        label = { Text(selectCropStr, color = FarmTextHint) },
                        modifier = Modifier.menuAnchor().fillMaxWidth(),
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = FarmOrangeSecondary,
                            unfocusedBorderColor = Color(0xFFDDDDDD),
                            focusedLabelColor = FarmOrangeSecondary,
                            focusedTextColor = FarmTextPrimary,
                            unfocusedTextColor = FarmTextPrimary
                        ),
                        shape = RoundedCornerShape(12.dp)
                    )
                    ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                        crops.forEach { crop ->
                            DropdownMenuItem(
                                text = { Text(DataTranslator.translate(crop.crop), color = FarmTextPrimary) },
                                onClick = { selectedCrop = crop; expanded = false; showResults = false }
                            )
                        }
                    }
                }
            }

            Spacer(Modifier.height(14.dp))

            // Land size card
            PremiumSectionCard {
                Text(stringResource(R.string.cost_step_2, landSizeStr), fontSize = 13.sp, color = FarmTextSecondary, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(12.dp))
                OutlinedTextField(
                    value = landSize,
                    onValueChange = { landSize = it; showResults = false },
                    label = { Text(landSizeStr, color = FarmTextHint) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = FarmOrangeSecondary,
                        unfocusedBorderColor = Color(0xFFDDDDDD),
                        focusedLabelColor = FarmOrangeSecondary,
                        focusedTextColor = FarmTextPrimary,
                        unfocusedTextColor = FarmTextPrimary
                    ),
                    shape = RoundedCornerShape(12.dp)
                )
            }

            Spacer(Modifier.height(24.dp))

            // Calculate button
            Button(
                onClick = {
                    val size = landSize.toDoubleOrNull()
                    if (size != null && selectedCrop != null) {
                        val (cost, yield) = viewModel.calculate(selectedCrop!!, size)
                        resultCost = cost
                        resultYield = yield
                        resultRevenue = yield * 25.0  // ₹25/kg avg market price
                        showResults = true
                    }
                },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = FarmOrangeSecondary),
                enabled = selectedCrop != null && landSize.isNotEmpty()
            ) {
                Text(calculateStr, fontSize = 17.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)
            }

            Spacer(Modifier.height(28.dp))

            // Results
            AnimatedVisibility(
                visible = showResults,
                enter = fadeIn(tween(350)) + slideInVertically(tween(400), initialOffsetY = { 60 })
            ) {
                Column {
                    Text(resultsStr, fontSize = 18.sp, fontWeight = FontWeight.ExtraBold, color = FarmTextPrimary)
                    Spacer(Modifier.height(16.dp))

                    // Metric cards grid
                    val profit = resultRevenue - resultCost
                    val metrics = listOf(
                        Triple(totalCostStr,    "₹${String.format("%.0f", resultCost)}",     Color(0xFFD32F2F)),
                        Triple(expectedYieldStr,"${String.format("%.0f", resultYield)} kg",  FarmGreenMid),
                        Triple(revenueStr,      "₹${String.format("%.0f", resultRevenue)}",  StatusInfo),
                        Triple(profitStr,       "₹${String.format("%.0f", profit)}",          if (profit >= 0) StatusSuccess else StatusDanger)
                    )
                    metrics.chunked(2).forEach { row ->
                        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            row.forEach { (label, value, color) ->
                                Card(
                                    modifier = Modifier.weight(1f),
                                    shape = RoundedCornerShape(16.dp),
                                    colors = CardDefaults.cardColors(containerColor = FarmSurface),
                                    elevation = CardDefaults.cardElevation(3.dp)
                                ) {
                                    Column(Modifier.padding(16.dp)) {
                                        Text(label, fontSize = 11.sp, color = FarmTextSecondary, fontWeight = FontWeight.Medium)
                                        Spacer(Modifier.height(8.dp))
                                        Text(value, fontSize = 22.sp, fontWeight = FontWeight.ExtraBold, color = color)
                                    }
                                }
                            }
                        }
                        Spacer(Modifier.height(12.dp))
                    }

                    // ROI bar
                    if (resultCost > 0) {
                        val roi = (resultRevenue - resultCost) / resultCost
                        Spacer(Modifier.height(4.dp))
                        Card(shape = RoundedCornerShape(16.dp), colors = CardDefaults.cardColors(containerColor = FarmSurface), elevation = CardDefaults.cardElevation(3.dp), modifier = Modifier.fillMaxWidth()) {
                            Column(Modifier.padding(16.dp)) {
                                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                    Text(stringResource(R.string.cost_roi), fontSize = 13.sp, color = FarmTextSecondary, fontWeight = FontWeight.Bold)
                                    Text("${String.format("%.0f", roi * 100)}%", fontSize = 15.sp, fontWeight = FontWeight.ExtraBold, color = if (roi >= 0) StatusSuccess else StatusDanger)
                                }
                                Spacer(Modifier.height(10.dp))
                                LinearProgressIndicator(
                                    progress = { minOf(1f, maxOf(0f, roi.toFloat())) },
                                    modifier = Modifier.fillMaxWidth().height(10.dp).clip(RoundedCornerShape(5.dp)),
                                    color = if (roi >= 0) FarmGreenAccent else StatusDanger,
                                    trackColor = Color(0xFFEEEEEE)
                                )
                            }
                        }
                    }
                    Spacer(Modifier.height(40.dp))
                }
            }
        }
    }
}

@Composable
fun PremiumSectionCard(content: @Composable ColumnScope.() -> Unit) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = FarmSurface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp), content = content)
    }
}
