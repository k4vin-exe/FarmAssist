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

data class NewsArticle(val title: String, val author: String, val date: String, val content: String, val tag: String)

val dummyNews = listOf(
    NewsArticle(
        "Govt Announces New Subsidies for Drip Irrigation",
        "Agri India", "25 Mar 2026", "POLICY",
        "Farmers in South India are now eligible for up to 80% subsidies on advanced Drip Irrigation sensors when using organic fertilizers. Applications accepted at district agriculture offices until June 2026."
    ),
    NewsArticle(
        "South-West Monsoon 2026: Above Normal Prediction",
        "Meteorological Dept", "24 Mar 2026", "WEATHER",
        "The IMD predicts above-normal rainfall for TN Kharif season. Delta districts — Thanjavur, Nagapattinam, and Tiruvarur — expected to receive 15% above long-period average. Rice farmers advised to prepare nurseries early."
    ),
    NewsArticle(
        "New Pest-Resistant Cotton Variety Released by TNAU",
        "TNAU Research", "20 Mar 2026", "RESEARCH",
        "A highly productive cotton biological variant immune to bollworms is now available from TNAU seed banks. The variety showed 30% higher yield in trials across Erode and Virudhunagar districts."
    ),
    NewsArticle(
        "PM-KISAN 17th Installment: ₹2000 Released to Farmers",
        "PIB India", "15 Mar 2026", "SCHEMES",
        "The PM-KISAN 17th installment of ₹2,000 has been transferred to 9.2 crore eligible farmers. Check your Aadhaar-linked bank account. Ineligible beneficiaries will need to submit e-KYC at nearest CSC centre."
    ),
    NewsArticle(
        "eNAM Onboarded 200 New Commodities Across Tamil Nadu",
        "eNAM Portal", "10 Mar 2026", "MARKET",
        "The National Agriculture Market platform has onboarded 45 new Tamil Nadu mandis and expanded commodity coverage. Farmers can now sell turmeric, drumstick, and banana directly to registered buyers across 22 states."
    )
)

private val tagColors = mapOf(
    "POLICY"   to Color(0xFF1565C0),
    "WEATHER"  to Color(0xFF00838F),
    "RESEARCH" to Color(0xFF558B2F),
    "SCHEMES"  to Color(0xFFF57F17),
    "MARKET"   to Color(0xFF6A1B9A)
)

@Composable
fun NewsScreen() {
    val newsTitle    = stringResource(R.string.news_title)
    val newsSubtitle = stringResource(R.string.news_subtitle)
    val byLabel      = stringResource(R.string.news_by)
    val updatesStr   = stringResource(R.string.news_updates_today)

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
                Text(newsTitle, color = Color.White, fontSize = 24.sp, fontWeight = FontWeight.ExtraBold)
                Spacer(Modifier.height(4.dp))
                Text(newsSubtitle, color = Color.White.copy(alpha = 0.82f), fontSize = 13.sp)
                Spacer(Modifier.height(16.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Notifications, contentDescription = null, tint = Color.White.copy(alpha = 0.7f), modifier = Modifier.size(14.dp))
                    Spacer(Modifier.width(6.dp))
                    Text("${dummyNews.size} $updatesStr", color = Color.White.copy(alpha = 0.7f), fontSize = 12.sp)
                }
            }
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(horizontal = 18.dp, vertical = 18.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            items(dummyNews.size) { i ->
                val article = dummyNews[i]
                val tagColor = tagColors[article.tag] ?: FarmGreenMid
                NewsCard(article = article, tagColor = tagColor, byLabel = byLabel)
            }
            item { Spacer(Modifier.height(20.dp)) }
        }
    }
}

@Composable
fun NewsCard(article: NewsArticle, tagColor: Color, byLabel: String) {
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
                    Text(article.date, fontSize = 11.sp, color = FarmTextHint)
                }
                Spacer(Modifier.height(12.dp))
                Text(article.title, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = FarmTextPrimary, lineHeight = 22.sp)
                Spacer(Modifier.height(8.dp))
                Text(article.content, fontSize = 13.sp, color = FarmTextSecondary, lineHeight = 19.sp)
                Spacer(Modifier.height(14.dp))
                HorizontalDivider(color = Color(0xFFF0F0F0))
                Spacer(Modifier.height(10.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(modifier = Modifier.size(28.dp).clip(CircleShape).background(tagColor.copy(alpha = 0.15f)), contentAlignment = Alignment.Center) {
                        Icon(Icons.Default.Info, contentDescription = null, tint = tagColor, modifier = Modifier.size(16.dp))
                    }
                    Spacer(Modifier.width(10.dp))
                    Column {
                        Text("$byLabel ${article.author}", fontSize = 12.sp, color = FarmTextSecondary, fontWeight = FontWeight.Medium)
                    }
                }
            }
        }
    }
}
