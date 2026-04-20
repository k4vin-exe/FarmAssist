package com.farmassist.data.remote

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

// RSS2JSON Response Models
data class NewsResponse(val status: String, val feed: FeedData, val items: List<NewsItem>)
data class FeedData(val url: String, val title: String, val link: String)
data class NewsItem(
    val title: String,
    val pubDate: String,
    val link: String,
    val guid: String,
    val author: String?,
    val thumbnail: String?,
    val description: String,
    val content: String,
    val categories: List<String>
)

interface NewsApi {
    @GET("v1/api.json")
    suspend fun getLatestAgriNews(
        @Query("rss_url") rssUrl: String = "https://www.thehindubusinessline.com/economy/agri-business/feeder/default.rss"
    ): NewsResponse

    companion object {
        const val BASE_URL = "https://api.rss2json.com/"

        fun create(): NewsApi {
            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            return retrofit.create(NewsApi::class.java)
        }
    }
}
