package com.farmassist.util

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONArray
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder

object LiveTranslator {
    suspend fun translate(text: String, targetLang: String): String {
        if (targetLang == "en") return text
        if (text.isBlank()) return text
        
        return withContext(Dispatchers.IO) {
            try {
                // To avoid URL too long issues, we might need to handle huge texts carefully, 
                // but RSS titles/descriptions are usually <1000 chars.
                val chunkedText = if (text.length > 2000) text.take(2000) else text
                val urlEncodedText = URLEncoder.encode(chunkedText, "UTF-8")
                val urlString = "https://translate.googleapis.com/translate_a/single?client=gtx&sl=auto&tl=$targetLang&dt=t&q=$urlEncodedText"
                
                val url = URL(urlString)
                val connection = url.openConnection() as HttpURLConnection
                connection.requestMethod = "GET"
                connection.setRequestProperty("User-Agent", "Mozilla/5.0")
                connection.connectTimeout = 3000
                connection.readTimeout = 5000

                if (connection.responseCode == 200) {
                    val response = connection.inputStream.bufferedReader().use { it.readText() }
                    // Response format: [[["தமிழ்","English",null,null,1]],null,"en"]
                    val jsonArray = JSONArray(response)
                    val chunks = jsonArray.getJSONArray(0)
                    val sb = StringBuilder()
                    for (i in 0 until chunks.length()) {
                        sb.append(chunks.getJSONArray(i).getString(0))
                    }
                    sb.toString().trim()
                } else {
                    text // Fallback
                }
            } catch (e: Exception) {
                e.printStackTrace()
                text // Fallback on failure
            }
        }
    }
}
