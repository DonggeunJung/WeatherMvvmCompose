package com.example.weathermvvmcompose.presentation

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.weathermvvmcompose.common.RequestPermissionsUtil
import com.example.weathermvvmcompose.data.weather.Weather
import com.example.weathermvvmcompose.ui.theme.EditBack
import com.example.weathermvvmcompose.ui.theme.WeatherMvvmComposeTheme
import com.google.android.gms.location.LocationServices
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : BaseActivity() {
    private val TAG = "MainActivity"
    @Inject
    lateinit var vm : WeatherViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            WeatherMvvmComposeTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(vm, modifier = Modifier.padding(1.dp), this, pref)
                }
            }
        }

        init()
    }

    override fun onStart() {
        super.onStart()
        RequestPermissionsUtil(this).requestLocation()
    }

    private fun init() {
        //observeLiveData()
        vm.reqStates()

        val pair = readPrefCity()
        if(pair.first.length >= 2 && pair.second.length >= 2) {
            vm.selState.value = pair.first
            vm.city.value = pair.second
            reqWetherByName()
        } else {
            getLocation()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode == RequestPermissionsUtil.REQUEST_LOCATION)
            getLocation()
    }

    @SuppressLint("MissingPermission")
    private fun getLocation() {
        val fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(this)

        fusedLocationProviderClient.lastLocation
            .addOnSuccessListener { success: Location? ->
                success?.let { location ->
                    onAchieveLocation(location.latitude, location.longitude)
                    Log.d(TAG, "${location.latitude}, ${location.longitude}")
                }
            }
            .addOnFailureListener { fail ->
                Log.d(TAG, fail.localizedMessage)
            }
    }

    private fun onAchieveLocation(lat: Double, lon: Double) {
        vm.reqWeatherLocation(lat, lon)
    }

    private fun readPrefCity(): Pair<String,String> {
        val state = pref.getString(KEY_STATE, "") ?: ""
        val city = pref.getString(KEY_CITY, "") ?: ""
        return Pair(state, city)
    }

    private fun reqWetherByName() {
        val state = vm.selState.value
        val city = vm.city.value
        if(state.isNullOrEmpty() || state.length < 2 || city.isNullOrEmpty() || city.length < 2) {
            Toast.makeText(this, "State & City name should be entered.", Toast.LENGTH_SHORT).show()
            return
        }

        // hide keyboard
        //Util.hideKeypad(this, binding.etCity)

        vm.reqWeather(city, state, pref)
    }

    companion object {
        const val KEY_STATE = "state"
        const val KEY_CITY = "city"
    }
}

@Composable
fun Greeting(vm : WeatherViewModel, modifier: Modifier = Modifier, context: Context, pref: SharedPreferences) {
    val keyboardCtrl = LocalSoftwareKeyboardController.current

    Box(modifier = modifier.fillMaxSize()) {
        Column(modifier = modifier.fillMaxSize().background(color = Color.White).padding(20.dp)) {
            Text(text = "State", color = Color.LightGray, modifier = modifier)

            TextField(value= vm.selState.value, onValueChange={},
                Modifier.fillMaxWidth().clickable { vm.statesVisible.value = true },
                enabled = false,
                colors = TextFieldDefaults.colors(disabledContainerColor = EditBack,
                    disabledIndicatorColor = Color.DarkGray, disabledTextColor = Color.DarkGray,
                    focusedTextColor = Color.DarkGray, unfocusedTextColor = Color.DarkGray))

            Spacer(modifier.height(20.dp))
            Text(text = "City", color = Color.LightGray, modifier = modifier)

            TextField(value= vm.city.value, onValueChange={ vm.city.value = it.trim()}, Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.colors(unfocusedContainerColor = EditBack, focusedContainerColor = EditBack,
                    focusedIndicatorColor = Color.DarkGray, unfocusedIndicatorColor = Color.DarkGray,
                    focusedTextColor = Color.DarkGray, unfocusedTextColor = Color.DarkGray))

            Spacer(modifier.height(20.dp))
            Button(modifier = modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp),
                onClick = {
                    keyboardCtrl?.hide()
                    vm.reqWeather(vm.city.value, vm.selState.value, pref)
                }) {
                Text(text = "Get weather")
            }

            vm.weather.value?.let { WeatherInfo(it, modifier, context) }

        }

        if(vm.statesVisible.value) {
            stateListPopup(vm, modifier)
        }
    }
}

@Composable
fun stateListPopup(vm : WeatherViewModel, modifier: Modifier = Modifier) {

    val borderModifier =
        Modifier.border(
            width = 0.5.dp,
            color = Color.LightGray,
            shape = RoundedCornerShape(12.dp)
        )

    Box(
        modifier.fillMaxHeight().width(100.dp)
            .padding(start = 20.dp, top = 20.dp, bottom = 80.dp)
            .background(color = Color.White).clip(RoundedCornerShape(12.dp))
            .then(borderModifier)
    ) {
        LazyColumn {
            items(vm.states) {
                Text(text = it.abbreviation,
                    modifier = Modifier.width(100.dp).padding(start = 10.dp, top = 10.dp)
                        .clickable {
                            vm.statesVisible.value = false
                            vm.selState.value = it.abbreviation
                        },
                    color = Color.DarkGray
                )
            }
        }
    }
}

@Composable
fun WeatherInfo(weather: Weather, modifier: Modifier, context: Context) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Spacer(modifier.height(34.dp))
        Text(text = weather.cityName(context), color = Color.DarkGray, fontSize = 24.sp)
        Spacer(modifier.height(15.dp))
        Row(modifier = Modifier.fillMaxWidth()) {
            Image(painter = rememberAsyncImagePainter(weather.iconUrl()),
                contentDescription = null, modifier = Modifier.size(50.dp).padding(top=4.dp))
            Text(text = weather.tempFahrenheit(context), color = Color.DarkGray, fontSize = 38.sp)
            Spacer(modifier.width(14.dp))
            Text(text = weather.minMax(context), modifier = modifier.padding(top=15.dp), color = Color.DarkGray, fontSize = 14.sp)
        }
        Spacer(modifier.height(10.dp))
        Row(modifier = Modifier.fillMaxWidth()) {
            Text(text = "Feels like ${weather.feelsLikeFahrenheit(context)}", color = Color.DarkGray, fontSize = 18.sp)
            Spacer(modifier.width(14.dp))
            Text(text = weather.weatherDescription(), color = Color.DarkGray, fontSize = 18.sp)
        }
        Spacer(modifier.height(10.dp))
        Row(modifier = Modifier.fillMaxWidth()) {
            Text(text = weather.windDescription(context), color = Color.DarkGray, fontSize = 14.sp)
            Spacer(modifier.width(14.dp))
            Text(text = weather.pressure(context), color = Color.DarkGray, fontSize = 14.sp)
        }
        Spacer(modifier.height(10.dp))
        Row(modifier = Modifier.fillMaxWidth()) {
            Text(text = weather.humidity(context), color = Color.DarkGray, fontSize = 14.sp)
            Spacer(modifier.width(14.dp))
            Text(text = weather.visibility(context), color = Color.DarkGray, fontSize = 14.sp)
        }
    }
}
