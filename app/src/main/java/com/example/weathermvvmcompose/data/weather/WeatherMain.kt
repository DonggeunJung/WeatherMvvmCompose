package com.example.weathermvvmcompose.data.weather

import kotlinx.serialization.Serializable

@Serializable
data class WeatherMain(var temp: Double, var feels_like: Double, var temp_min: Double, var temp_max: Double, var pressure: Double, var humidity: Int)