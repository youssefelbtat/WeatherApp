package com.example.weatherapp.data.repo

import com.example.weatherapp.data.model.Alerts
import com.example.weatherapp.data.model.LastWeather
import com.example.weatherapp.data.model.RootWeatherModel
import com.example.weatherapp.helper.Constants.APP_ID
import kotlinx.coroutines.flow.Flow

interface RepositoryInterface {
    suspend fun getRootWeatherFromAPI(
        latitude: Double, longitude: Double, appid: String = APP_ID, units: String,
        lang: String="en"
    ): Flow<RootWeatherModel>
    suspend fun getAllFavorites(): Flow<List<RootWeatherModel>>

    suspend fun insertFavorite(favorite: RootWeatherModel)

    suspend fun removeFavorite(favorite: RootWeatherModel)

    suspend fun getLastWeather(): Flow<LastWeather>

    suspend fun updateLastWeather(lastWeather: LastWeather)

    fun getAllAlerts(): Flow<List<Alerts>>

    suspend fun insertAlert(alert: Alerts)

    suspend fun removeAlert(alert: Alerts)
}