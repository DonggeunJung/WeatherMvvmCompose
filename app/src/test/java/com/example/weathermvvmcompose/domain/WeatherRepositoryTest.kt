package com.example.weathermvvmcompose.domain

import android.content.Context
import com.example.weathermvvmcompose.data.weather.WeatherApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.junit.Assert
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.mock
import com.example.weathermvvmcompose.R

class WeatherRepositoryTest {
    @Mock
    private val mockContext: Context = mock(Context::class.java)
    private val api: WeatherApi = WeatherApi.instance
    private val appId = WeatherRepository.appId ?: ""

    @Test
    fun weatherApi_should_return_weather_data() {
        CoroutineScope(Dispatchers.IO).launch {
            val weather = api.weather("Novi,MI,US", appId)
            Assert.assertNotNull(weather)
            Assert.assertFalse(weather?.weather.isNullOrEmpty())
            Assert.assertFalse(weather?.name.isNullOrEmpty())
            Assert.assertNotNull(weather?.main)
            Assert.assertNotNull(weather?.visibility)
        }
    }

    @Test
    fun weatherLocationApi_should_return_weather_data() {
        CoroutineScope(Dispatchers.IO).launch {
            val weather = api.weatherLocation(42.46, -83.49, appId)
            Assert.assertNotNull(weather)
            Assert.assertFalse(weather?.weather.isNullOrEmpty())
            Assert.assertFalse(weather?.name.isNullOrEmpty())
            Assert.assertNotNull(weather?.main)
            Assert.assertNotNull(weather?.visibility)
        }
    }

    @Test
    fun reqWeather_should_return_weather_data() {
        CoroutineScope(Dispatchers.IO).launch {
            val repository = WeatherRepository(mockContext, api)
            Mockito.`when`(mockContext.getString(R.string.country_code)).thenReturn("US")
            val weather = repository.reqWeather("Novi,MI")
            Assert.assertNotNull(weather)
            Assert.assertFalse(weather?.weather.isNullOrEmpty())
            Assert.assertFalse(weather?.name.isNullOrEmpty())
            Assert.assertNotNull(weather?.main)
            Assert.assertNotNull(weather?.visibility)
        }
    }

    @Test
    fun reqWeatherLocation_should_return_weather_data() {
        CoroutineScope(Dispatchers.IO).launch {
            val repository = WeatherRepository(mockContext, api)
            val weather = repository.reqWeatherLocation(42.46, -83.49)
            Assert.assertNotNull(weather)
            Assert.assertFalse(weather?.weather.isNullOrEmpty())
            Assert.assertFalse(weather?.name.isNullOrEmpty())
            Assert.assertNotNull(weather?.main)
            Assert.assertNotNull(weather?.visibility)
        }
    }
}