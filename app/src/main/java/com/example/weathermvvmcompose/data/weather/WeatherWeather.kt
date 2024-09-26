package com.example.weathermvvmcompose.data.weather

import kotlinx.serialization.Serializable

@Serializable
data class WeatherWeather(val id: Int, val main: String, var description: String, val icon: String)