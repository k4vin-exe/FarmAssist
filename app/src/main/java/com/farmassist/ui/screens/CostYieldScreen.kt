package com.farmassist.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.farmassist.data.local.model.Crop
import com.farmassist.ui.theme.*
import com.farmassist.ui.viewmodels.CostYieldViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CostYieldScreen(viewModel: CostYieldViewModel) {
    val crops by viewModel.crops.collectAsState()
    var selectedCrop by remember { mutableStateOf<Crop?>(null) }
    var expanded by remember { mutableStateOf(false) }
    var landSize by remember { mutableStateOf("") }
    
    var resultCost by remember { mutableStateOf(0.0) }
    var resultYield by remember { mutableStateOf(0.0) }
    var showResults by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Dynamic Header
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp))
                .background(FarmGreenPrimary)
                .padding(bottom = 24.dp, start = 24.dp, end = 24.dp, top = 16.dp)
        ) {
            Column {
                Text(
                    text = "Cost & Yield Calculator",
                    color = Color.White,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Plan your financial investments and predict your exact harvest returns in advance.",
                    color = Color.White.copy(alpha = 0.85f),
                    fontSize = 14.sp
                )
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
                .verticalScroll(rememberScrollState())
        ) {
            // Dropdown Menu for Crop Selection
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded },
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = selectedCrop?.crop ?: "Select Output Crop",
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Target Crop", color = FarmTextSecondary) },
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
                    crops.forEach { crop ->
                        DropdownMenuItem(
                            text = { Text(crop.crop, color = FarmTextPrimary) },
                            onClick = {
                                selectedCrop = crop
                                expanded = false
                                showResults = false
                            }
                        )
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(20.dp))

            // Land Size Input
            OutlinedTextField(
                value = landSize,
                onValueChange = { 
                    landSize = it 
                    showResults = false 
                },
                label = { Text("Land Size (in Acres)", color = FarmTextSecondary) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = FarmGreenPrimary,
                    unfocusedBorderColor = Color.Gray,
                    focusedLabelColor = FarmGreenPrimary,
                    focusedTextColor = FarmTextPrimary,
                    unfocusedTextColor = FarmTextPrimary
                ),
                shape = RoundedCornerShape(12.dp)
            )
            
            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = {
                    val size = landSize.toDoubleOrNull()
                    if (size != null && selectedCrop != null) {
                        val (cost, yield) = viewModel.calculate(selectedCrop!!, size)
                        resultCost = cost
                        resultYield = yield
                        showResults = true
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = FarmOrangeSecondary),
                enabled = selectedCrop != null && landSize.isNotEmpty()
            ) {
                Text("Calculate Estimate", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)
            }
            
            Spacer(modifier = Modifier.height(32.dp))

            AnimatedVisibility(
                visible = showResults,
                enter = fadeIn(tween(400)) + slideInVertically(tween(400), initialOffsetY = { 50 })
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "Estimation Dashboard", 
                        style = MaterialTheme.typography.titleLarge, 
                        fontWeight = FontWeight.Bold, 
                        color = FarmTextPrimary
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        // Cost Metric Card
                        Card(
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = FarmGreenLight),
                            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text("Initial Investment", fontSize = 13.sp, color = FarmTextSecondary)
                                Spacer(modifier = Modifier.height(12.dp))
                                Text("₹" + String.format("%.0f", resultCost), fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color(0xFFD32F2F))
                                Spacer(modifier = Modifier.height(4.dp))
                                Text("Seeds & Setup Cost", fontSize = 10.sp, color = FarmTextSecondary)
                            }
                        }

                        // Yield Metric Card
                        Card(
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = FarmGreenLight),
                            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text("Expected Harvest", fontSize = 13.sp, color = FarmTextSecondary)
                                Spacer(modifier = Modifier.height(12.dp))
                                Text(String.format("%.0f", resultYield) + " kg", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = FarmGreenPrimary)
                                Spacer(modifier = Modifier.height(4.dp))
                                Text("Estimated Volume", fontSize = 10.sp, color = FarmTextSecondary)
                            }
                        }
                    }
                }
            }
        }
    }
}
