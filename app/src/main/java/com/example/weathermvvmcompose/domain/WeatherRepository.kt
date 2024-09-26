package com.example.weathermvvmcompose.domain

import android.content.Context
import android.util.Log
import com.example.weathermvvmcompose.R
import com.example.weathermvvmcompose.common.Util
import com.example.weathermvvmcompose.data.weather.Weather
import com.example.weathermvvmcompose.data.weather.WeatherApi
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class WeatherRepository @Inject constructor(@ApplicationContext private val context: Context, private val api: WeatherApi) {

    suspend fun reqWeather(city: String): Weather? {
        appId ?: return null
        val code = context.getString(R.string.country_code)
        return try {
            api.weather("$city,$code", appId)
        } catch (e: Exception) {
            e.message?.let { Log.d("MovieRepository", it) }
            null
        }
    }

    suspend fun reqWeatherLocation(lat: Double, lon: Double): Weather? {
        appId ?: return null
        return try {
            api.weatherLocation(lat, lon, appId)
        } catch (e: Exception) {
            e.message?.let { Log.d("MovieRepository", it) }
            null
        }
    }

    companion object {
        private const val ENCODED_APP_ID = "SwNjc83GFItmXTs6INDYgVxzIdhw8VuwR3DxhDJXvZcFZLWsf5Oaogu9vYjy7luy"
        val appId: String? = Util.decrypt(ENCODED_APP_ID)
    }
}