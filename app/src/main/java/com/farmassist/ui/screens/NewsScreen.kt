package com.farmassist.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.farmassist.ui.theme.*

data class NewsArticle(val title: String, val author: String, val date: String, val content: String)

val dummyNews = listOf(
    NewsArticle(
        "Govt Announces New Subsidies for Drip Irrigation", 
        "Agri India", "25 Mar 2026", 
        "Farmers in South India are now eligible for up to 80% subsidies on highly advanced remote Drip Irrigation sensors if using organic fertilizers."
    ),
    NewsArticle(
        "South-West Monsoon Prediction Model for 2026", 
        "Met Dept", "24 Mar 2026", 
        "The upcoming South-West monsoon is predicted to be slightly above normal, resulting in extreme water stability for Rice farmers."
    ),
    NewsArticle(
        "New Pest Resistant Cotton Varietal Released", 
        "TNAU", "20 Mar 2026", 
        "A highly productive cotton biological variant strictly immune to bollworms is now organically available for local farmers."
    )
)

@Composable
fun NewsScreen() {
    Column(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
        Box(
            modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp)).background(FarmGreenPrimary).padding(bottom = 24.dp, start = 24.dp, end = 24.dp, top = 16.dp)
        ) {
            Column {
                Text("Agriculture News", color = Color.White, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(8.dp))
                Text("Stay updated with modern farming tech and market trends.", color = Color.White.copy(alpha = 0.85f), fontSize = 14.sp)
            }
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(dummyNews.size) { index ->
                val article = dummyNews[index]
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = FarmGreenLight),
                    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Row(verticalAlignment = Alignment.Top) {
                            Icon(Icons.Default.Info, contentDescription = null, tint = FarmOrangeSecondary, modifier = Modifier.size(24.dp).padding(top=2.dp))
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(article.title, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = FarmTextPrimary, lineHeight = 22.sp)
                                Spacer(modifier = Modifier.height(6.dp))
                                Text("By " + article.author + " • " + article.date, style = MaterialTheme.typography.bodySmall, color = FarmTextSecondary, fontWeight = FontWeight.Medium)
                            }
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(article.content, style = MaterialTheme.typography.bodyMedium, color = FarmTextSecondary, lineHeight = 20.sp)
                    }
                }
            }
        }
    }
}
