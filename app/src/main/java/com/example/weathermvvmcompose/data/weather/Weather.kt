package com.example.weathermvvmcompose.data.weather

import android.content.Context
import androidx.annotation.VisibleForTesting
import com.example.weathermvvmcompose.R
import com.example.weathermvvmcompose.common.Util
import kotlinx.serialization.Serializable

@Serializable
data class Weather(val weather: List<WeatherWeather>, val main: WeatherMain, var visibility: Int, val wind: WeatherWind, var name: String) {

    fun iconUrl(): String {
        return if(weather.isNullOrEmpty() || weather[0].icon.isNullOrEmpty()) ""
        else "https://openweathermap.org/img/wn/${weather[0].icon}.png"
    }

    fun minMax(context: Context): String {
        val max = cToF(main.temp_max)
        val min = cToF(main.temp_min)
        val unit = context.getString(R.string.fahrenheit_unit)
        return "$max / $min${unit}"
    }

    fun cityName(context: Context): String {
        val code = context.getString(R.string.country_code)
        return "${this.name}, $code"
    }

    fun visibility(context: Context): String {
        var km = this.visibility.toDouble() / 1000
        km = Util.roundDigit(km, 1)
        val title = context.getString(R.string.visibility)
        val unit = context.getString(R.string.km)
        return "$title: ${km}${unit}"
    }

    fun weatherDescription(): String {
        if(weather.isNullOrEmpty() || weather[0].description.isNullOrEmpty()) return ""
        return weather[0].description[0].uppercase() + weather[0].description.substring(1)
    }

    fun humidity(context: Context): String {
        val title = context.getString(R.string.humidity)
        val unit = context.getString(R.string.percent)
        return "$title: ${this.main.humidity}${unit}"
    }

    fun pressure(context: Context): String {
        val unit = context.getString(R.string.press_unit)
        return "${this.main.pressure}${unit}"
    }

    fun tempFahrenheit(context: Context): String {
        val unit = context.getString(R.string.fahrenheit_unit)
        return "${cToF(main.temp)}${unit}"
    }

    fun feelsLikeFahrenheit(context: Context): String {
        val unit = context.getString(R.string.fahrenheit_unit)
        return "${cToF(main.feels_like)}${unit}"
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal fun cToF(cel: Double): Int {
        return ((cel - 273.15) * 9.0 / 5.0).toInt() + 32
    }

    fun windDescription(context: Context): String {
        val v = Util.roundDigit(this.wind.speed, 1).toString()
        val unit = context.getString(R.string.wind_unit)
        val windDir = windDir()
        return "${v}${unit} $windDir"
    }

    internal fun windDir(): String {
        val unit = 90.0 / 4.0; var curr = unit
        val news = listOf("N", "NEN", "NE", "ENE", "E", "ESE", "SE", "SES",
            "S", "SWS", "SW", "WSW", "W", "WNW", "WN", "NWN")
        val deg = (this.wind.deg.toDouble() + unit/2.0) % 360.0
        for(i in news.indices) {
            if(deg < curr) return news[i]
            curr += unit
        }
        return news[0]
    }
}