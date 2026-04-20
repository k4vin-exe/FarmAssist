package com.farmassist.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
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
import com.farmassist.util.DataTranslator

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.farmassist.data.local.model.NewsEntity
import com.farmassist.ui.viewmodels.NewsViewModel
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.filled.Refresh

private val tagColors = mapOf(
    "Agri Business" to Color(0xFF1565C0),
    "News"          to Color(0xFF00838F),
    "Companies"     to Color(0xFF558B2F),
    "Market"        to Color(0xFFF57F17),
    "Agri News"     to Color(0xFF6A1B9A)
)

@Composable
fun NewsScreen(viewModel: NewsViewModel) {
    val newsTitle    = stringResource(R.string.news_title)
    val newsSubtitle = stringResource(R.string.news_subtitle)
    val byLabel      = stringResource(R.string.news_by)
    val updatesStr   = stringResource(R.string.news_updates_today)

    val newsList by viewModel.newsState.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val errorMsg by viewModel.errorMessage.collectAsState()

    Column(modifier = Modifier.fillMaxSize().background(FarmBackground)) {
        // Header
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Brush.verticalGradient(listOf(GradientOrangeEnd, GradientOrangeStart)))
                .padding(start = 24.dp, end = 24.dp, top = 20.dp, bottom = 28.dp)
        ) {
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("📰", fontSize = 14.sp)
                    Spacer(Modifier.width(6.dp))
                    Text("LATEST NEWS", color = Color.White.copy(alpha = 0.80f), fontSize = 11.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.5.sp)
                }
                Spacer(Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(newsTitle, color = Color.White, fontSize = 24.sp, fontWeight = FontWeight.ExtraBold)
                    IconButton(onClick = { viewModel.refreshNews() }) {
                        Icon(Icons.Default.Refresh, contentDescription = "Refresh", tint = Color.White)
                    }
                }
                Spacer(Modifier.height(4.dp))
                Text(newsSubtitle, color = Color.White.copy(alpha = 0.82f), fontSize = 13.sp)
                Spacer(Modifier.height(16.dp))
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Notifications, contentDescription = null, tint = Color.White.copy(alpha = 0.7f), modifier = Modifier.size(14.dp))
                        Spacer(Modifier.width(6.dp))
                        Text("${newsList.size} $updatesStr", color = Color.White.copy(alpha = 0.7f), fontSize = 12.sp)
                    }
                    if (isLoading) {
                        CircularProgressIndicator(modifier = Modifier.size(16.dp), color = Color.White, strokeWidth = 2.dp)
                    }
                }
            }
        }

        if (errorMsg != null && newsList.isNotEmpty()) {
            Surface(color = FarmOrangeSecondary, modifier = Modifier.fillMaxWidth()) {
                Text(errorMsg!!, color = Color.White, fontSize = 12.sp, modifier = Modifier.padding(8.dp), fontWeight = FontWeight.Medium)
            }
        }

        if (newsList.isEmpty() && !isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("No news available currently. Check your connection.", color = FarmTextSecondary, fontSize = 14.sp)
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(horizontal = 18.dp, vertical = 18.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                items(newsList) { article ->
                    val tagColor = tagColors[article.tag] ?: FarmGreenMid
                    NewsCard(article = article, tagColor = tagColor, byLabel = byLabel)
                }
                item { Spacer(Modifier.height(20.dp)) }
            }
        }
    }
}

@Composable
fun NewsCard(article: NewsEntity, tagColor: Color, byLabel: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = FarmSurface),
        elevation = CardDefaults.cardElevation(3.dp)
    ) {
        Column {
            // Coloured top accent bar
            Box(modifier = Modifier.fillMaxWidth().height(4.dp).background(tagColor))
            Column(modifier = Modifier.padding(18.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Category tag
                    Surface(shape = RoundedCornerShape(6.dp), color = tagColor.copy(alpha = 0.12f)) {
                        Text(DataTranslator.translate(article.tag), color = tagColor, fontSize = 10.sp, fontWeight = FontWeight.ExtraBold, modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp))
                    }
                    Text(article.pubDate, fontSize = 11.sp, color = FarmTextHint)
                }
                Spacer(Modifier.height(12.dp))
                Text(article.title, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = FarmTextPrimary, lineHeight = 22.sp)
                Spacer(Modifier.height(8.dp))
                Text(article.description, fontSize = 13.sp, color = FarmTextSecondary, lineHeight = 19.sp)
                Spacer(Modifier.height(14.dp))
                HorizontalDivider(color = Color(0xFFF0F0F0))
                Spacer(Modifier.height(10.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(modifier = Modifier.size(28.dp).clip(CircleShape).background(tagColor.copy(alpha = 0.15f)), contentAlignment = Alignment.Center) {
                        Icon(Icons.Default.Info, contentDescription = null, tint = tagColor, modifier = Modifier.size(16.dp))
                    }
                    Spacer(Modifier.width(10.dp))
                    Column {
                        Text("$byLabel The Hindu Business Line", fontSize = 12.sp, color = FarmTextSecondary, fontWeight = FontWeight.Medium)
                    }
                }
            }
        }
    }
}
