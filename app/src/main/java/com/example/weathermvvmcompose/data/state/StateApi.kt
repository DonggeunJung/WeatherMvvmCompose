package com.example.weathermvvmcompose.data.state

import com.example.weathermvvmcompose.common.Util
import retrofit2.http.GET

interface StateApi {
    companion object {
        private const val BASE_URL = "https://gist.githubusercontent.com/mshafrir/2646763/raw/8b0dbb93521f5d6889502305335104218454c2bf/"

        val instance: StateApi = Util.buildRetrofit(BASE_URL).create(StateApi::class.java)
    }

    @GET("states_titlecase.json")
    suspend fun states(): List<State>
}