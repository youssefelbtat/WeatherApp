package com.example.weatherapp.data.source

import com.example.weatherapp.data.model.Alerts
import com.example.weatherapp.data.model.LastWeather
import com.example.weatherapp.data.model.RootWeatherModel
import com.example.weatherapp.data.source.db.LocalSourceInterface
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class FakeLocalSource : LocalSourceInterface {
    private val favorites: MutableList<RootWeatherModel> = mutableListOf()
    private val lastWeather = LastWeather()
    private val alerts: MutableList<Alerts> = mutableListOf()

    override suspend fun getAllFavorites(): Flow<List<RootWeatherModel>> {
        return flowOf(favorites)
    }

    override suspend fun insertFavorite(favorite: RootWeatherModel) {
        favorites.add(favorite)
    }

    override suspend fun removeFavorite(favorite: RootWeatherModel) {
        favorites.remove(favorite)
    }

    override suspend fun getLastWeather(): Flow<LastWeather> {
        return flowOf(lastWeather)
    }

    override suspend fun updateLastWeather(lastWeather: LastWeather) {
        this.lastWeather.lat = lastWeather.lat
        this.lastWeather.lon = lastWeather.lon
    }

    override fun getAllAlerts(): Flow<List<Alerts>> {
        return flowOf(alerts)
    }

    override suspend fun insertAlert(alert: Alerts) {
        alerts.add(alert)
    }

    override suspend fun removeAlert(alert: Alerts) {
        alerts.remove(alert)
    }
}
