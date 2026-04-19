package com.farmassist.ui.screens

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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

    var searchQuery by remember { mutableStateOf("") }
    var selectedSunlight by remember { mutableStateOf("All") }
    var selectedDifficulty by remember { mutableStateOf("All") }
    var selectedCrop by remember { mutableStateOf<com.farmassist.data.local.model.TerraceFarming?>(null) }
    val bottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var showSheet by remember { mutableStateOf(false) }

    val sunlightOptions = listOf("All", "Full", "Partial", "Shade")
    val difficultyOptions = listOf("All", "Easy", "Medium", "Hard")

    val filtered = items.filter { crop ->
        val matchesSearch = crop.crop.contains(searchQuery, ignoreCase = true)
        val matchesSun = selectedSunlight == "All" || crop.sunlight.equals(selectedSunlight, ignoreCase = true)
        val matchesDiff = selectedDifficulty == "All" || crop.difficulty.equals(selectedDifficulty, ignoreCase = true)
        matchesSearch && matchesSun && matchesDiff
    }

    val myGardenItems = items.filter { myTerraceCrops.contains(it.crop) }

    // Bottom sheet detail view
    if (showSheet && selectedCrop != null) {
        val crop = selectedCrop!!
        val isPlanted = myTerraceCrops.contains(crop.crop)
        ModalBottomSheet(
            onDismissRequest = { showSheet = false },
            sheetState = bottomSheetState,
            containerColor = MaterialTheme.colorScheme.background,
            shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 24.dp)
                    .padding(bottom = 32.dp)
            ) {
                // Emoji + Name header
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(crop.emoji, fontSize = 48.sp)
                    Spacer(Modifier.width(16.dp))
                    Column {
                        Text(
                            DataTranslator.translate(crop.crop),
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            color = FarmTextPrimary
                        )
                        Spacer(Modifier.height(4.dp))
                        DifficultyBadge(crop.difficulty)
                    }
                }
                Spacer(Modifier.height(16.dp))

                // Stats row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    TerraceStatChip(
                        icon = "☀️",
                        label = DataTranslator.translate(crop.sunlight),
                        color = Color(0xFFF59E0B),
                        modifier = Modifier.weight(1f)
                    )
                    TerraceStatChip(
                        icon = "💧",
                        label = DataTranslator.translate(crop.water),
                        color = Color(0xFF3B82F6),
                        modifier = Modifier.weight(1f)
                    )
                    TerraceStatChip(
                        icon = "📅",
                        label = "${crop.days}d",
                        color = FarmGreenPrimary,
                        modifier = Modifier.weight(1f)
                    )
                }
                Spacer(Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    TerraceStatChip(
                        icon = "📦",
                        label = DataTranslator.translate(crop.containerSize),
                        color = Color(0xFF8B5CF6),
                        modifier = Modifier.weight(1f)
                    )
                    Spacer(Modifier.weight(2f))
                }
                Spacer(Modifier.height(16.dp))

                // Description
                if (crop.description.isNotEmpty()) {
                    Card(
                        colors = CardDefaults.cardColors(containerColor = FarmGreenLight),
                        shape = RoundedCornerShape(14.dp)
                    ) {
                        Text(
                            crop.description,
                            fontSize = 14.sp,
                            color = FarmTextPrimary,
                            lineHeight = 22.sp,
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                    Spacer(Modifier.height(16.dp))
                }

                // Tips
                if (crop.tips.isNotEmpty()) {
                    Text("💡 Growing Tips", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = FarmTextPrimary)
                    Spacer(Modifier.height(10.dp))
                    crop.tips.forEachIndexed { idx, tip ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 10.dp),
                            verticalAlignment = Alignment.Top
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(26.dp)
                                    .clip(CircleShape)
                                    .background(FarmGreenPrimary),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    "${idx + 1}",
                                    color = Color.White,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            Spacer(Modifier.width(12.dp))
                            Text(
                                DataTranslator.translate(tip),
                                fontSize = 14.sp,
                                color = FarmTextSecondary,
                                lineHeight = 20.sp,
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                    Spacer(Modifier.height(8.dp))
                }

                // Add/Remove button
                Button(
                    onClick = {
                        viewModel.toggleTerraceCrop(crop.crop, !isPlanted)
                        showSheet = false
                    },
                    modifier = Modifier.fillMaxWidth().height(52.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isPlanted) Color(0xFFFFEBEE) else FarmGreenPrimary,
                        contentColor = if (isPlanted) Color(0xFFD32F2F) else Color.White
                    )
                ) {
                    Icon(
                        imageVector = if (isPlanted) Icons.Default.Delete else Icons.Default.Add,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(
                        text = if (isPlanted) "Remove from My Garden" else "Add to My Garden",
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp
                    )
                }
            }
        }
    }

    Column(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
        // Header
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp))
                .background(
                    Brush.linearGradient(listOf(GradientPurpleStart, GradientPurpleEnd))
                )
                .padding(bottom = 20.dp, start = 20.dp, end = 20.dp, top = 16.dp)
        ) {
            Column {
                Text("🏡 Terrace Garden", color = Color.White, fontSize = 22.sp, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(4.dp))
                Text(
                    "Growing ${myGardenItems.size} crops • ${items.size} available",
                    color = Color.White.copy(alpha = 0.85f),
                    fontSize = 13.sp
                )
                Spacer(Modifier.height(14.dp))
                // Search bar
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = { Text("Search crops...", color = Color.White.copy(alpha = 0.6f), fontSize = 14.sp) },
                    leadingIcon = {
                        Icon(Icons.Default.Search, contentDescription = null, tint = Color.White.copy(alpha = 0.7f))
                    },
                    trailingIcon = {
                        if (searchQuery.isNotEmpty()) {
                            IconButton(onClick = { searchQuery = "" }) {
                                Icon(Icons.Default.Close, contentDescription = null, tint = Color.White.copy(alpha = 0.7f))
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color.White.copy(alpha = 0.5f),
                        unfocusedBorderColor = Color.White.copy(alpha = 0.3f),
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        cursorColor = Color.White
                    ),
                    singleLine = true
                )
            }
        }

        androidx.compose.foundation.lazy.LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Filter chips row
            item {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    // Sunlight filter
                    Row(
                        modifier = Modifier
                            .horizontalScroll(rememberScrollState())
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            "☀️",
                            fontSize = 14.sp,
                            modifier = Modifier.align(Alignment.CenterVertically)
                        )
                        sunlightOptions.forEach { opt ->
                            FilterChip(
                                selected = selectedSunlight == opt,
                                onClick = { selectedSunlight = opt },
                                label = { Text(DataTranslator.translate(opt), fontSize = 12.sp) },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = GradientPurpleStart,
                                    selectedLabelColor = Color.White
                                ),
                                shape = RoundedCornerShape(20.dp)
                            )
                        }
                    }
                    // Difficulty filter
                    Row(
                        modifier = Modifier
                            .horizontalScroll(rememberScrollState())
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            "🌱",
                            fontSize = 14.sp,
                            modifier = Modifier.align(Alignment.CenterVertically)
                        )
                        difficultyOptions.forEach { opt ->
                            val chipColor = when (opt) {
                                "Easy" -> Color(0xFF22C55E)
                                "Medium" -> Color(0xFFF59E0B)
                                "Hard" -> Color(0xFFEF4444)
                                else -> GradientPurpleStart
                            }
                            FilterChip(
                                selected = selectedDifficulty == opt,
                                onClick = { selectedDifficulty = opt },
                                label = { Text(DataTranslator.translate(opt), fontSize = 12.sp) },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = chipColor,
                                    selectedLabelColor = Color.White
                                ),
                                shape = RoundedCornerShape(20.dp)
                            )
                        }
                    }
                }
            }

            // My Garden section
            if (myGardenItems.isNotEmpty()) {
                item {
                    Text("🏡 My Garden", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = FarmTextPrimary)
                }
                item {
                    Row(
                        modifier = Modifier
                            .horizontalScroll(rememberScrollState())
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        myGardenItems.forEach { crop ->
                            MyGardenChip(
                                crop = crop,
                                onRemove = { viewModel.toggleTerraceCrop(crop.crop, false) },
                                onClick = { selectedCrop = crop; showSheet = true }
                            )
                        }
                    }
                }
                item {
                    HorizontalDivider(
                        modifier = Modifier.padding(vertical = 4.dp),
                        color = Color.LightGray.copy(alpha = 0.4f)
                    )
                }
            }

            // Browse section header
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("🌿 Browse Plants", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = FarmTextPrimary)
                    Text("${filtered.size} results", fontSize = 12.sp, color = FarmTextSecondary)
                }
            }

            if (filtered.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier.fillMaxWidth().padding(32.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("🔍", fontSize = 48.sp)
                            Spacer(Modifier.height(8.dp))
                            Text("No crops match your filters", color = FarmTextSecondary, fontSize = 14.sp)
                        }
                    }
                }
            }

            // Crop cards - 2 column grid simulation via pairs
            val pairs = filtered.chunked(2)
            items(pairs) { pair ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    pair.forEach { crop ->
                        TerraceCropCard(
                            crop = crop,
                            isPlanted = myTerraceCrops.contains(crop.crop),
                            modifier = Modifier.weight(1f),
                            onClick = { selectedCrop = crop; showSheet = true }
                        )
                    }
                    if (pair.size == 1) Spacer(Modifier.weight(1f))
                }
            }
        }
    }
}

@Composable
private fun TerraceCropCard(
    crop: com.farmassist.data.local.model.TerraceFarming,
    isPlanted: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier
            .clickable(
                interactionSource = remember { androidx.compose.foundation.interaction.MutableInteractionSource() },
                indication = null
            ) { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isPlanted) Color(0xFFF0FDF4) else Color.White
        ),
        border = if (isPlanted)
            androidx.compose.foundation.BorderStroke(2.dp, FarmGreenPrimary.copy(alpha = 0.6f))
        else
            androidx.compose.foundation.BorderStroke(1.dp, Color.LightGray.copy(alpha = 0.5f)),
        elevation = CardDefaults.cardElevation(defaultElevation = if (isPlanted) 4.dp else 2.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Text(crop.emoji, fontSize = 36.sp)
                if (isPlanted) {
                    Surface(
                        shape = CircleShape,
                        color = FarmGreenPrimary
                    ) {
                        Icon(
                            Icons.Default.Check,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(18.dp).padding(3.dp)
                        )
                    }
                }
            }
            Spacer(Modifier.height(8.dp))
            Text(
                DataTranslator.translate(crop.crop),
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = FarmTextPrimary,
                maxLines = 1
            )
            Spacer(Modifier.height(6.dp))
            Row(
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                DifficultyDot(crop.difficulty)
                Text(crop.difficulty, fontSize = 11.sp, color = FarmTextSecondary)
            }
            Spacer(Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                MiniStatBadge("☀️", crop.sunlight.take(4))
                MiniStatBadge("💧", crop.water.take(3))
                MiniStatBadge("📦", crop.containerSize.take(3))
            }
            Spacer(Modifier.height(6.dp))
            Text(
                "🕐 ${crop.days} days to harvest",
                fontSize = 11.sp,
                color = FarmTextSecondary
            )
        }
    }
}

@Composable
private fun MyGardenChip(
    crop: com.farmassist.data.local.model.TerraceFarming,
    onRemove: () -> Unit,
    onClick: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = FarmGreenLight,
        border = androidx.compose.foundation.BorderStroke(1.dp, FarmGreenPrimary.copy(alpha = 0.4f)),
        modifier = Modifier.clickable(
            interactionSource = remember { androidx.compose.foundation.interaction.MutableInteractionSource() },
            indication = null
        ) { onClick() }
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(crop.emoji, fontSize = 18.sp)
            Spacer(Modifier.width(6.dp))
            Text(
                DataTranslator.translate(crop.crop),
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                color = FarmGreenPrimary
            )
            Spacer(Modifier.width(6.dp))
            IconButton(
                onClick = onRemove,
                modifier = Modifier.size(18.dp)
            ) {
                Icon(
                    Icons.Default.Close,
                    contentDescription = null,
                    tint = Color.Gray,
                    modifier = Modifier.size(14.dp)
                )
            }
        }
    }
}

@Composable
private fun TerraceStatChip(icon: String, label: String, color: Color, modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(10.dp),
        color = color.copy(alpha = 0.1f)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(icon, fontSize = 14.sp)
            Spacer(Modifier.width(6.dp))
            Text(label, fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = color)
        }
    }
}

@Composable
private fun DifficultyBadge(difficulty: String) {
    val (color, emoji) = when (difficulty) {
        "Easy" -> Pair(Color(0xFF22C55E), "🟢")
        "Medium" -> Pair(Color(0xFFF59E0B), "🟡")
        "Hard" -> Pair(Color(0xFFEF4444), "🔴")
        else -> Pair(Color.Gray, "⚪")
    }
    Surface(shape = RoundedCornerShape(8.dp), color = color.copy(alpha = 0.12f)) {
        Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(emoji, fontSize = 11.sp)
            Spacer(Modifier.width(4.dp))
            Text(difficulty, fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = color)
        }
    }
}

@Composable
private fun DifficultyDot(difficulty: String) {
    val color = when (difficulty) {
        "Easy" -> Color(0xFF22C55E)
        "Medium" -> Color(0xFFF59E0B)
        "Hard" -> Color(0xFFEF4444)
        else -> Color.Gray
    }
    Box(modifier = Modifier.size(8.dp).clip(CircleShape).background(color))
}

@Composable
private fun MiniStatBadge(icon: String, label: String) {
    Surface(shape = RoundedCornerShape(6.dp), color = Color(0xFFF5F5F5)) {
        Row(
            modifier = Modifier.padding(horizontal = 5.dp, vertical = 3.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(icon, fontSize = 9.sp)
            Spacer(Modifier.width(2.dp))
            Text(label, fontSize = 9.sp, color = FarmTextSecondary, fontWeight = FontWeight.Medium)
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
