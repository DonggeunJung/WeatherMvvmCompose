package com.example.weathermvvmcompose.data.di

import android.content.Context
import com.example.weathermvvmcompose.data.state.StateApi
import com.example.weathermvvmcompose.data.state.StateDatabase
import com.example.weathermvvmcompose.data.weather.WeatherApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideContext(@ApplicationContext context: Context): Context {
        return context
    }

    @Singleton
    @Provides
    fun provideStateDatabase(@ApplicationContext context: Context): StateDatabase {
        return StateDatabase.getInstance(context)
    }

    @Singleton
    @Provides
    fun provideStateApi(): StateApi {
        // Create Retrofit API object & return
        return StateApi.instance
    }

    @Singleton
    @Provides
    fun provideWeatherApi(): WeatherApi {
        // Create Retrofit API object & return
        return WeatherApi.instance
    }

}