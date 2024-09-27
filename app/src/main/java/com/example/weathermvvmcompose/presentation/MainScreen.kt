package com.example.weathermvvmcompose.presentation

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.weathermvvmcompose.data.weather.Weather
import com.example.weathermvvmcompose.ui.theme.EditBack

@Composable
fun Greeting(vm : WeatherViewModel, modifier: Modifier = Modifier, req:()->Unit) {

    Box(modifier = modifier.fillMaxSize()) {
        Column(modifier = modifier
            .fillMaxSize()
            .background(color = Color.White)
            .padding(20.dp)) {
            UserInput(vm, modifier, req)

            vm.weather.value?.let { WeatherInfo(it, modifier) }
        }

        if(vm.statesVisible.value) {
            StateListPopup(vm, modifier)
        }
    }
}

@Composable
fun UserInput(vm: WeatherViewModel, modifier: Modifier = Modifier, req:()->Unit) {

    Text(text = "State", color = Color.LightGray, modifier = modifier)

    TextField(value= vm.selState.value, onValueChange={},
        Modifier
            .fillMaxWidth()
            .clickable { vm.statesVisible.value = true },
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
        onClick = { req() }) {
        Text(text = "Get weather")
    }

}

@Composable
fun StateListPopup(vm : WeatherViewModel, modifier: Modifier = Modifier) {

    val borderModifier =
        Modifier.border(
            width = 0.5.dp,
            color = Color.LightGray,
            shape = RoundedCornerShape(12.dp)
        )

    Box(
        modifier
            .fillMaxHeight()
            .width(100.dp)
            .padding(start = 20.dp, top = 20.dp, bottom = 80.dp)
            .background(color = Color.White)
            .clip(RoundedCornerShape(12.dp))
            .then(borderModifier)
    ) {
        LazyColumn {
            items(vm.states) {
                Text(text = it.abbreviation,
                    modifier = Modifier
                        .width(100.dp)
                        .padding(start = 10.dp, top = 10.dp)
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
fun WeatherInfo(weather: Weather, modifier: Modifier) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Spacer(modifier.height(34.dp))
        Text(text = weather.cityName(), color = Color.DarkGray, fontSize = 24.sp)
        Spacer(modifier.height(15.dp))
        Row(modifier = Modifier.fillMaxWidth()) {
            Image(painter = rememberAsyncImagePainter(weather.iconUrl()),
                contentDescription = null, modifier = Modifier
                    .size(50.dp)
                    .padding(top = 4.dp))
            Text(text = weather.tempFahrenheit(), color = Color.DarkGray, fontSize = 38.sp)
            Spacer(modifier.width(14.dp))
            Text(text = weather.minMax(), modifier = modifier.padding(top=15.dp), color = Color.DarkGray, fontSize = 14.sp)
        }
        Spacer(modifier.height(10.dp))
        Row(modifier = Modifier.fillMaxWidth()) {
            Text(text = "Feels like ${weather.feelsLikeFahrenheit()}", color = Color.DarkGray, fontSize = 18.sp)
            Spacer(modifier.width(14.dp))
            Text(text = weather.weatherDescription(), color = Color.DarkGray, fontSize = 18.sp)
        }
        Spacer(modifier.height(10.dp))
        Row(modifier = Modifier.fillMaxWidth()) {
            Text(text = weather.windDescription(), color = Color.DarkGray, fontSize = 14.sp)
            Spacer(modifier.width(14.dp))
            Text(text = weather.pressure(), color = Color.DarkGray, fontSize = 14.sp)
        }
        Spacer(modifier.height(10.dp))
        Row(modifier = Modifier.fillMaxWidth()) {
            Text(text = weather.humidity(), color = Color.DarkGray, fontSize = 14.sp)
            Spacer(modifier.width(14.dp))
            Text(text = weather.visibility(), color = Color.DarkGray, fontSize = 14.sp)
        }
    }
}
