package com.farmassist.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.platform.LocalContext
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import com.farmassist.data.local.model.Crop
import com.farmassist.ui.theme.*
import com.farmassist.ui.viewmodels.CropEstimationState
import com.farmassist.ui.viewmodels.CropViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CropEstimationScreen(viewModel: CropViewModel) {
    val uiState by viewModel.uiState.collectAsState()
    val allDistricts by viewModel.allDistricts.collectAsState()
    val context = LocalContext.current

    var expanded by remember { mutableStateOf(false) }
    var selectedDistrict by remember { mutableStateOf("Select Offline District") }

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) viewModel.estimateCropAuto()
        else viewModel.setError("Location Permission Denied. Please select offline.")
    }

    LaunchedEffect(Unit) {
        if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            viewModel.estimateCropAuto()
        } else {
            viewModel.setError("Location required for Auto. Click 'Auto GPS' to permit, or use Dropdown.")
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
                Text(
                    text = "AI Crop Estimation",
                    color = Color.White,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Button(
                        onClick = { 
                            if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                                viewModel.estimateCropAuto()
                            } else {
                                permissionLauncher.launch(android.Manifest.permission.ACCESS_FINE_LOCATION)
                            }
                        },
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = FarmOrangeSecondary)
                    ) {
                        Text("Auto GPS")
                    }

                    // Dropdown for manual selection
                    ExposedDropdownMenuBox(
                        expanded = expanded,
                        onExpandedChange = { expanded = !expanded },
                        modifier = Modifier.weight(1f)
                    ) {
                        OutlinedTextField(
                            value = selectedDistrict,
                            onValueChange = {},
                            readOnly = true,
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Color.White,
                                unfocusedBorderColor = Color.White,
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White
                            ),
                            textStyle = LocalTextStyle.current.copy(fontSize = 14.sp),
                            modifier = Modifier.menuAnchor().fillMaxWidth()
                        )
                        ExposedDropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false }
                        ) {
                            if (allDistricts.isEmpty()) {
                                DropdownMenuItem(text = { Text("Initializing Database...") }, onClick = {})
                            } else {
                                allDistricts.forEach { districtObj ->
                                    DropdownMenuItem(
                                        text = { Text(districtObj.district, color = Color.Black) },
                                        onClick = {
                                            selectedDistrict = districtObj.district
                                            expanded = false
                                            viewModel.estimateCropManual(districtObj.district)
                                        }
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
                Box(modifier = Modifier.fillMaxSize().padding(32.dp), contentAlignment = Alignment.Center) {
                    Text(
                        text = "Hit Auto GPS or select a district manually to work entirely offline!",
                        color = FarmTextSecondary,
                        textAlign = TextAlign.Center
                    )
                }
            }
            is CropEstimationState.Loading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = FarmGreenPrimary)
                }
            }
            is CropEstimationState.Error -> {
                Box(modifier = Modifier.fillMaxSize().padding(32.dp), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = Icons.Default.Info, 
                            contentDescription = "Info", 
                            tint = FarmOrangeSecondary,
                            modifier = Modifier.size(48.dp)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = state.message.replace("Error: ", ""), 
                            color = FarmTextSecondary, 
                            textAlign = TextAlign.Center,
                            fontSize = 16.sp
                        )
                    }
                }
            }
            is CropEstimationState.Success -> {
                Column(modifier = Modifier.padding(16.dp)) {
                    // Conditions Overview Card
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = FarmGreenLight),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            if (state.defaultUsed) {
                                Text(
                                    text = "Offline Mode: Using historical district metrics",
                                    fontSize = 12.sp,
                                    color = FarmTextSecondary,
                                    modifier = Modifier.padding(bottom = 8.dp)
                                )
                            }
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceEvenly
                            ) {
                                ConditionBadge("Soil", state.soil)
                                ConditionBadge("Temp", state.temp.toString() + "°C")
                                ConditionBadge("Season", state.season)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))
                    Text(text = "Top Recommendations:", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.SemiBold, color = FarmTextPrimary)
                    Spacer(modifier = Modifier.height(12.dp))

                    if (state.recommendations.isEmpty()) {
                        Text(text = "No exact matches found. Showing general suggestions.", color = FarmTextSecondary)
                    }

                    LazyColumn(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                        items(state.recommendations) { crop ->
                            CropResultCard(crop = crop)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ConditionBadge(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = label, fontSize = 12.sp, color = FarmTextSecondary)
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = value, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = FarmGreenPrimary)
    }
}

@Composable
fun CropResultCard(crop: Crop) {
    Card(
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = crop.crop, fontSize = 22.sp, fontWeight = FontWeight.Bold, color = FarmGreenPrimary)
                Surface(
                    shape = RoundedCornerShape(50),
                    color = FarmOrangeSecondary.copy(alpha = 0.2f)
                ) {
                    Text(
                        text = crop.growing_days.toString() + " Days",
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        color = FarmOrangeSecondary,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Column {
                    Text(text = "Investment (/Acre)", fontSize = 12.sp, color = FarmTextSecondary)
                    Text(text = "₹" + crop.cost_per_acre, fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text(text = "Est. Yield (/Acre)", fontSize = 12.sp, color = FarmTextSecondary)
                    Text(text = crop.yield_per_acre.toString() + " kg", fontSize = 16.sp, fontWeight = FontWeight.SemiBold, color = FarmGreenPrimary)
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = "Optimal Temperature: " + crop.temp_min + "°C - " + crop.temp_max + "°C", fontSize = 12.sp, color = FarmTextSecondary)
            Spacer(modifier = Modifier.height(4.dp))
            LinearProgressIndicator(
                progress = 0.7f,
                modifier = Modifier.fillMaxWidth().height(8.dp).clip(RoundedCornerShape(4.dp)),
                color = FarmGreenPrimary,
                trackColor = FarmGreenLight
            )
        }
    }
}
