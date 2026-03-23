package com.farmassist.app.data.remote

import android.util.Xml
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.xmlpull.v1.XmlPullParser
import java.net.HttpURLConnection
import java.net.URL

data class NewsItem(val title: String, val link: String, val description: String)

class RssFeedService {
    
    suspend fun fetchAgriNews(rssUrl: String): List<NewsItem> = withContext(Dispatchers.IO) {
        val newsList = mutableListOf<NewsItem>()
        try {
            val url = URL(rssUrl)
            val connection = url.openConnection() as HttpURLConnection
            connection.readTimeout = 10000
            connection.connectTimeout = 15000
            connection.requestMethod = "GET"
            connection.doInput = true
            connection.connect()
            
            val parser = Xml.newPullParser()
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false)
            parser.setInput(connection.inputStream, null)
            
            var title = ""
            var link = ""
            var description = ""
            var isItem = false
            
            var eventType = parser.eventType
            while (eventType != XmlPullParser.END_DOCUMENT) {
                val name = parser.name ?: ""
                when (eventType) {
                    XmlPullParser.START_TAG -> {
                        if (name.equals("item", ignoreCase = true)) {
                            isItem = true
                        } else if (isItem) {
                            when (name.lowercase()) {
                                "title" -> title = parser.nextText()
                                "link" -> link = parser.nextText()
                                "description" -> description = parser.nextText()
                            }
                        }
                    }
                    XmlPullParser.END_TAG -> {
                        if (name.equals("item", ignoreCase = true)) {
                            newsList.add(NewsItem(title, link, description))
                            isItem = false
                            title = ""
                            link = ""
                            description = ""
                        }
                    }
                }
                eventType = parser.next()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return@withContext newsList
    }
}
