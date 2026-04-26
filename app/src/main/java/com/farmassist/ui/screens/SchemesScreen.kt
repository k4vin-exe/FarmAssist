package com.farmassist.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.farmassist.R
import com.farmassist.data.local.model.Scheme
import com.farmassist.ui.theme.*
import com.farmassist.ui.viewmodels.InfoViewModel
import com.farmassist.util.DataTranslator

// Colour palette for scheme cards (cycles through)
private val schemeGradients = listOf(
    listOf(Color(0xFF1B5E20), Color(0xFF2E7D32)),   // Deep green
    listOf(Color(0xFFE65100), Color(0xFFF57F17)),   // Warm orange
    listOf(Color(0xFF0D47A1), Color(0xFF1976D2)),   // Royal blue
    listOf(Color(0xFF4A148C), Color(0xFF7B1FA2)),   // Purple
    listOf(Color(0xFF006064), Color(0xFF0097A7)),   // Teal
    listOf(Color(0xFF880E4F), Color(0xFFC2185B)),   // Rose
    listOf(Color(0xFF1A237E), Color(0xFF3949AB)),   // Indigo
    listOf(Color(0xFF33691E), Color(0xFF558B2F)),   // Lime green
)

private val schemeIcons = listOf(
    Icons.Default.Home,
    Icons.Default.Star,
    Icons.Default.CheckCircle,
    Icons.Default.Refresh,
    Icons.Default.Info,
    Icons.Default.Favorite,
    Icons.AutoMirrored.Filled.List,
    Icons.Default.Place
)

@Composable
fun SchemesScreen(viewModel: InfoViewModel) {
    val items by viewModel.schemes.collectAsState()
    val schemesTitle = stringResource(R.string.schemes_title)
    val schemesSubtitle = stringResource(R.string.schemes_subtitle)
    val benefitLabel = stringResource(R.string.schemes_benefit_label)
    val eligibilityLabel = stringResource(R.string.schemes_eligibility_label)

    Column(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {

        // Header with gradient
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(Color(0xFF1B5E20), Color(0xFF2E7D32))
                    )
                )
                .padding(start = 24.dp, end = 24.dp, top = 20.dp, bottom = 28.dp)
        ) {
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Home, contentDescription = null, tint = Color(0xFFA5D6A7), modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(stringResource(R.string.schemes_title_caps), color = Color(0xFFA5D6A7), fontSize = 11.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.5.sp)
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(schemesTitle, color = Color.White, fontSize = 28.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(6.dp))
                Text(schemesSubtitle, color = Color.White.copy(alpha = 0.80f), fontSize = 14.sp, lineHeight = 20.sp)
                Spacer(modifier = Modifier.height(20.dp))

                // Quick stat row
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    QuickStat("${items.size}", "Active Schemes")
                    QuickStat("₹", "Direct Benefits")
                    QuickStat("Free", "Applications")
                }
            }
        }

        // Scheme cards
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(horizontal = 20.dp, vertical = 20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            itemsIndexed(items) { index, scheme ->
                SchemeCard(
                    scheme = scheme,
                    index = index,
                    benefitLabel = benefitLabel,
                    eligibilityLabel = eligibilityLabel
                )
            }
            item { Spacer(modifier = Modifier.height(16.dp)) }
        }
    }
}

@Composable
fun QuickStat(value: String, label: String) {
    Surface(
        shape = RoundedCornerShape(10.dp),
        color = Color.White.copy(alpha = 0.15f)
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(value, color = Color.White, fontWeight = FontWeight.ExtraBold, fontSize = 18.sp)
            Text(label, color = Color.White.copy(alpha = 0.80f), fontSize = 10.sp)
        }
    }
}

@Composable
fun SchemeCard(scheme: Scheme, index: Int, benefitLabel: String, eligibilityLabel: String) {
    var expanded by remember { mutableStateOf(false) }
    val gradient = schemeGradients[index % schemeGradients.size]
    val icon = schemeIcons[index % schemeIcons.size]

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(elevation = 4.dp, shape = RoundedCornerShape(20.dp))
            .animateContentSize(animationSpec = tween(300)),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column {
            // Coloured header band
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        brush = Brush.linearGradient(gradient)
                    )
                    .padding(20.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    // Icon circle
                    Box(
                        modifier = Modifier.size(48.dp).clip(CircleShape).background(Color.White.copy(alpha = 0.20f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(icon, contentDescription = null, tint = Color.White, modifier = Modifier.size(26.dp))
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        // Scheme number badge
                        Surface(shape = RoundedCornerShape(4.dp), color = Color.White.copy(alpha = 0.20f)) {
                            Text(stringResource(R.string.schemes_scheme_prefix, index + 1), color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp))
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(DataTranslator.translate(scheme.name), color = Color.White, fontSize = 15.sp, fontWeight = FontWeight.Bold, lineHeight = 20.sp)
                    }
                    // Expand/collapse chevron
                    Icon(
                        imageVector = if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                        contentDescription = "Toggle",
                        tint = Color.White,
                        modifier = Modifier
                            .size(28.dp)
                            .clip(CircleShape)
                            .clickable { expanded = !expanded }
                            .padding(2.dp)
                    )
                }
            }

            // Collapsed preview — always show benefit
            Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 16.dp)) {
                Row(verticalAlignment = Alignment.Top) {
                    Icon(Icons.Default.Star, contentDescription = null, tint = Color(0xFFF57F17), modifier = Modifier.size(18.dp).padding(top = 2.dp))
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(benefitLabel, fontSize = 11.sp, color = Color(0xFF757575), fontWeight = FontWeight.Bold, letterSpacing = 0.5.sp)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(DataTranslator.translate(scheme.benefit), fontSize = 14.sp, color = Color(0xFF212121), lineHeight = 20.sp)
                    }
                }

                // Expand/collapse eligibility section
                AnimatedVisibility(
                    visible = expanded,
                    enter = fadeIn(tween(200)) + expandVertically(tween(300)),
                    exit = fadeOut(tween(150)) + shrinkVertically(tween(250))
                ) {
                    Column {
                        Spacer(modifier = Modifier.height(16.dp))
                        HorizontalDivider(color = Color(0xFFEEEEEE))
                        Spacer(modifier = Modifier.height(16.dp))

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.CheckCircle, contentDescription = null, tint = gradient[0], modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(eligibilityLabel, fontSize = 13.sp, color = gradient[0], fontWeight = FontWeight.ExtraBold, letterSpacing = 0.5.sp)
                        }
                        Spacer(modifier = Modifier.height(12.dp))

                        // Render each bullet point as its own styled row
                        scheme.eligibility.split("\n").forEach { line ->
                            if (line.trim().isNotEmpty()) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(bottom = 8.dp)
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(Color(0xFFF5F5F5))
                                        .padding(horizontal = 12.dp, vertical = 10.dp),
                                    verticalAlignment = Alignment.Top
                                ) {
                                    Box(
                                        modifier = Modifier.size(6.dp).clip(CircleShape).background(gradient[0]).padding(top = 6.dp)
                                    )
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Text(DataTranslator.translate(line.removePrefix("•").trim()), fontSize = 13.sp, color = Color(0xFF424242), lineHeight = 19.sp)
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))
                // "Tap to expand" hint when collapsed
                if (!expanded) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .clickable { expanded = true }
                            .padding(vertical = 8.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("View Eligibility", fontSize = 13.sp, color = gradient[0], fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(Icons.Default.KeyboardArrowDown, contentDescription = null, tint = gradient[0], modifier = Modifier.size(18.dp))
                    }
                }
            }
        }
    }
}
