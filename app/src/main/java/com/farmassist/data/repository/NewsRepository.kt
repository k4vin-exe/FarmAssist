package com.farmassist.data.repository

import com.farmassist.data.local.dao.NewsDao
import com.farmassist.data.local.model.NewsEntity
import com.farmassist.data.remote.NewsApi
import kotlinx.coroutines.flow.Flow
import java.text.SimpleDateFormat
import java.util.Locale

class NewsRepository(
    private val newsDao: NewsDao,
    private val newsApi: NewsApi
) {
    // Expose offline cache instantly
    fun getNews(): Flow<List<NewsEntity>> = newsDao.getNews()

    suspend fun refreshNews() {
        try {
            val response = newsApi.getLatestAgriNews()
            if (response.status == "ok") {
                val newsEntities = response.items.map { item ->
                    NewsEntity(
                        guid = item.guid,
                        title = item.title,
                        pubDate = item.pubDate,
                        link = item.link,
                        description = item.description,
                        tag = item.categories.firstOrNull() ?: "Agri News"
                    )
                }
                
                // Clear old news and cache new ones
                if (newsEntities.isNotEmpty()) {
                    newsDao.deleteAllNews()
                    newsDao.insertNews(newsEntities)
                }
            }
        } catch (e: Exception) {
            // Log or handle error, allowing offline cache to stay active
            e.printStackTrace()
            throw e
        }
    }
}
