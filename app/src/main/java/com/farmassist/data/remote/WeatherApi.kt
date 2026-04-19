package com.farmassist.data.remote

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

// OpenWeather JSON response models
data class WeatherResponse(val main: MainData, val weather: List<WeatherInfo>)
data class MainData(val temp: Double, val humidity: Int)
data class WeatherInfo(val main: String, val description: String)

// Forecast Models
data class ForecastResponse(val list: List<ForecastItem>?)
data class ForecastItem(val dt: Long, val main: MainData, val weather: List<WeatherInfo>, val pop: Double = 0.0)

interface WeatherApi {
    @GET("weather")
    suspend fun getCurrentWeather(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("appid") apiKey: String,
        @Query("units") units: String = "metric"
    ): WeatherResponse

    @GET("forecast")
    suspend fun getForecast(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("appid") apiKey: String,
        @Query("units") units: String = "metric"
    ): ForecastResponse

    companion object {
        const val BASE_URL = "https://api.openweathermap.org/data/2.5/"

        fun create(): WeatherApi {
            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            return retrofit.create(WeatherApi::class.java)
        }
    }
}
