package com.farmassist.data.remote

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

data class SchemeDto(
    val name: String,
    val benefit: String,
    val eligibility: String
)

interface SchemesApi {
    
    // Attempt to download the latest agricultural schemes
    @GET("schemes.json")
    suspend fun getLatestSchemes(): List<SchemeDto>

    companion object {
        // Standard architectural mockup URL. Replace with real Firebase endpoint.
        const val BASE_URL = "https://raw.githubusercontent.com/FarmAssist/mock-backend/main/"

        fun create(): SchemesApi {
            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            return retrofit.create(SchemesApi::class.java)
        }
    }
}
