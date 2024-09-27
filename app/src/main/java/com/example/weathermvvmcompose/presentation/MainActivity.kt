package com.example.weathermvvmcompose.presentation

import android.annotation.SuppressLint
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import com.example.weathermvvmcompose.common.RequestPermissionsUtil
import com.example.weathermvvmcompose.common.Util
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
                val keyboardCtrl = LocalSoftwareKeyboardController.current
                Greeting(vm, modifier = Modifier.padding(1.dp)) {
                    keyboardCtrl?.hide()
                    vm.reqWeather()
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
        vm.reqStates()

        val pair = Util.readPrefCity()
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

    private fun reqWetherByName() {
        val state = vm.selState.value
        val city = vm.city.value
        if(state.isNullOrEmpty() || state.length < 2 || city.isNullOrEmpty() || city.length < 2) {
            Toast.makeText(this, "State & City name should be entered.", Toast.LENGTH_SHORT).show()
            return
        }

        vm.reqWeather()
    }

}
