package com.example.weathermvvmcompose.data.weather

import android.content.Context
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.doReturn
import org.mockito.Mockito.mock
import com.example.weathermvvmcompose.R
import com.example.weathermvvmcompose.presentation.App
import org.mockito.Mockito.`when`

class WeatherTest {
    @Mock
    private val mockContext: Context = mock(Context::class.java)
    private lateinit var weather: Weather

    @Before
    fun setup() {
        val weatherWeather = WeatherWeather(id=801, main="Clouds", description="few clouds", icon="02d")
        val main = WeatherMain(temp=295.07, feels_like=294.52, temp_min=293.27, temp_max=295.81, pressure=1022.0, humidity=46)
        val visibility = 10000
        val wind = WeatherWind(speed=1.34, deg=307)
        val name = "Novi"
        weather = Weather(weather=listOf(weatherWeather), main=main, visibility=visibility, wind=wind, name=name)
        App.context = mockContext
    }

    @Test
    fun iconUrl_should_return_url_of_icon() {
        val url = weather.iconUrl()
        Assert.assertEquals("https://openweathermap.org/img/wn/02d.png", url)
    }

    @Test
    fun cToF_should_return_fahrenheit() {
        val absolute = 273.15
        Assert.assertEquals(32, weather.cToF(0.0+absolute))
        Assert.assertEquals(41, weather.cToF(5.0+absolute))
        Assert.assertEquals(50, weather.cToF(10.0+absolute))
    }

    @Test
    fun minMax_should_return_temperature_range() {
        weather.main.temp_min = 293.27
        weather.main.temp_max = 295.81
        doReturn("°F").`when`(mockContext)?.getString(R.string.fahrenheit_unit)
        val res = weather.minMax()
        Assert.assertEquals("72 / 68°F", res)
    }

    @Test
    fun cityName_should_return_the_name_of_city_with_country_code() {
        weather.name = "New York"
        doReturn("US").`when`(mockContext)?.getString(R.string.country_code)
        val res = weather.cityName()
        Assert.assertEquals("New York, US", res)
    }

    @Test
    fun visibility_should_return_range_of_visibility_with_unit() {
        weather.visibility = 10000
        doReturn("Visibility").`when`(mockContext)?.getString(R.string.visibility)
        doReturn("km").`when`(mockContext)?.getString(R.string.km)
        val res = weather.visibility()
        Assert.assertEquals("Visibility: 10.0km", res)
    }

    @Test
    fun weatherDescription_should_return_capital_expression() {
        weather.weather[0].description = "few clouds"
        val res = weather.weatherDescription()
        Assert.assertEquals("Few clouds", res)
    }

    @Test
    fun humidity_should_return_humidity_with_unit() {
        weather.main.humidity = 46
        doReturn("Humidity").`when`(mockContext)?.getString(R.string.humidity)
        doReturn("%").`when`(mockContext)?.getString(R.string.percent)
        val res = weather.humidity()
        Assert.assertEquals("Humidity: 46%", res)
    }

    @Test
    fun pressure_should_return_pressure_with_unit() {
        weather.main.pressure = 1022.0
        doReturn("hPa").`when`(mockContext)?.getString(R.string.press_unit)
        val res = weather.pressure()
        Assert.assertEquals("1022.0hPa", res)
    }

    @Test
    fun tempFahrenheit_should_return_temperature_of_fahrenheit() {
        weather.main.temp = 295.07
        doReturn("°F").`when`(mockContext)?.getString(R.string.fahrenheit_unit)
        val res = weather.tempFahrenheit()
        Assert.assertEquals("71°F", res)
    }

    @Test
    fun feelsLikeFahrenheit_should_return_feelsLike_temperature_of_fahrenheit() {
        weather.main.feels_like = 295.07
        doReturn("°F").`when`(mockContext)?.getString(R.string.fahrenheit_unit)
        val res = weather.feelsLikeFahrenheit()
        Assert.assertEquals("71°F", res)
    }

    @Test
    fun windDescription_should_return_wind_speed_with_unit_and_direction() {
        weather.wind.speed = 1.34
        weather.wind.deg = 307
        doReturn("m/s").`when`(mockContext)?.getString(R.string.wind_unit)
        val res = weather.windDescription()
        Assert.assertEquals("1.3m/s WN", res)
    }

    @Test
    fun windDir_should_return_direction() {
        val unit = 90.0 / 4.0; var curr = 0.0;
        weather.wind.deg = curr.toInt()
        Assert.assertEquals(weather.windDir(), "N")
        curr += unit
        weather.wind.deg = curr.toInt()
        Assert.assertEquals(weather.windDir(), "NEN")
        curr += unit
        weather.wind.deg = curr.toInt()
        Assert.assertEquals(weather.windDir(), "NE")
        curr += unit
        weather.wind.deg = curr.toInt()
        Assert.assertEquals(weather.windDir(), "ENE")
        curr += unit
        weather.wind.deg = curr.toInt()
        Assert.assertEquals(weather.windDir(), "E")
        curr += unit
        weather.wind.deg = curr.toInt()
        Assert.assertEquals(weather.windDir(), "ESE")
        curr += unit
        weather.wind.deg = curr.toInt()
        Assert.assertEquals(weather.windDir(), "SE")
        curr += unit
        weather.wind.deg = curr.toInt()
        Assert.assertEquals(weather.windDir(), "SES")
        curr += unit
        weather.wind.deg = curr.toInt()
        Assert.assertEquals(weather.windDir(), "S")
        curr += unit
        weather.wind.deg = curr.toInt()
        Assert.assertEquals(weather.windDir(), "SWS")
        curr += unit
        weather.wind.deg = curr.toInt()
        Assert.assertEquals(weather.windDir(), "SW")
        curr += unit
        weather.wind.deg = curr.toInt()
        Assert.assertEquals(weather.windDir(), "WSW")
        curr += unit
        weather.wind.deg = curr.toInt()
        Assert.assertEquals(weather.windDir(), "W")
        curr += unit
        weather.wind.deg = curr.toInt()
        Assert.assertEquals(weather.windDir(), "WNW")
        curr += unit
        weather.wind.deg = curr.toInt()
        Assert.assertEquals(weather.windDir(), "WN")
        curr += unit
        weather.wind.deg = curr.toInt()
        Assert.assertEquals(weather.windDir(), "NWN")
    }
}