package com.example.weathermvvmcompose.presentation

import android.content.SharedPreferences
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weathermvvmcompose.data.State.State
import com.example.weathermvvmcompose.data.weather.Weather
import com.example.weathermvvmcompose.domain.StateRepository
import com.example.weathermvvmcompose.domain.WeatherRepository
import com.example.weathermvvmcompose.presentation.MainActivity.Companion.KEY_CITY
import com.example.weathermvvmcompose.presentation.MainActivity.Companion.KEY_STATE
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class WeatherViewModel @Inject constructor(private val stateRepository: StateRepository, private val weatherRepository: WeatherRepository) : ViewModel() {
    //private val ldStates = MutableLiveData<List<State>>()
    //val states: LiveData<List<State>> = ldStates
    //private val ldWeather = MutableLiveData<Weather>()
    //val weather: LiveData<Weather> = ldWeather
    val city = mutableStateOf("")
    var selState = mutableStateOf("")
    var statesVisible = mutableStateOf(false)

    private val _states = mutableStateListOf<State>()
    val states: List<State> = _states
    private val _weather = mutableStateOf<Weather?>(null)
    val weather: androidx.compose.runtime.State<Weather?> = _weather

    // Request Card data list to Repository
    fun reqStates() {
        viewModelScope.launch {
            val res = stateRepository.reqStates()

            withContext(Dispatchers.Main) {
                res?.let { _states.addAll(it) }
            }
        }
    }

    // Request Weather data by City name
    fun reqWeather(city: String, state: String, pref: SharedPreferences) {
        viewModelScope.launch {
            val res = weatherRepository.reqWeather("$city,$state")

            withContext(Dispatchers.Main) {
                res?.let {
                    _weather.value = it
                    savePrefCity(state, city, pref)
                }
            }
        }
    }

    private fun savePrefCity(state: String, city: String, pref: SharedPreferences) {
        val editor = pref.edit()
        editor.putString(KEY_STATE, state)
        editor.putString(KEY_CITY, city)
        editor.apply()
    }

    // Request Weather data by location
    fun reqWeatherLocation(lat: Double, lon: Double) {
        viewModelScope.launch {
            val res = weatherRepository.reqWeatherLocation(lat, lon)

            withContext(Dispatchers.Main) {
                res?.let { _weather.value = it }
            }
        }
    }

}