package com.example.weathermvvmcompose.data.weather

import kotlinx.serialization.Serializable

@Serializable
data class WeatherWind(var speed: Double, var deg: Int)