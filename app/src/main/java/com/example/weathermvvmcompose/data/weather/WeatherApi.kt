package com.example.weathermvvmcompose.data.weather

import com.example.weathermvvmcompose.common.Util
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {
    companion object {
        private const val BASE_URL = "https://api.openweathermap.org/data/2.5/"

        val instance: WeatherApi = Util.buildRetrofit(BASE_URL).create(WeatherApi::class.java)
    }

    @GET("weather")
    suspend fun weather(@Query("q") city: String, @Query("appid") appId: String): Weather

    @GET("weather")
    suspend fun weatherLocation(@Query("lat") lat: Double, @Query("lon") lon: Double, @Query("appid") appId: String): Weather
}