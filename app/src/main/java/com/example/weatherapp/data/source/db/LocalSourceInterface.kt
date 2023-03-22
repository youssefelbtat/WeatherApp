package com.example.weatherapp.data.source.db

import com.example.weatherapp.data.model.Alerts
import com.example.weatherapp.data.model.LastWeather
import com.example.weatherapp.data.model.RootWeatherModel
import kotlinx.coroutines.flow.Flow

interface LocalSourceInterface {

    suspend fun getAllFavorites(): Flow<List<RootWeatherModel>>

    suspend fun insertFavorite(favorite: RootWeatherModel)

    suspend fun removeFavorite(favorite: RootWeatherModel)

    suspend fun getLastWeather(): Flow<LastWeather>

    suspend fun updateLastWeather(lastWeather: LastWeather)

    fun getAllAlerts(): Flow<List<Alerts>>

    suspend fun insertAlert(alert: Alerts)

    suspend fun removeAlert(alert: Alerts)
}