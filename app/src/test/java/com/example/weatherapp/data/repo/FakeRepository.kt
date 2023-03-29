package com.example.weatherapp.data.repo

import com.example.weatherapp.data.model.Alerts
import com.example.weatherapp.data.model.LastWeather
import com.example.weatherapp.data.model.RootWeatherModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class FakeRepository : RepositoryInterface {
     private val favorites = mutableListOf<RootWeatherModel>()
    private var lastWeather: LastWeather? = null
    private val alerts = mutableListOf<Alerts>()

    override suspend fun getRootWeatherFromAPI(
        latitude: Double,
        longitude: Double,
        appid: String,
        units: String,
        lang: String
    ): Flow<RootWeatherModel> {
        return flowOf(RootWeatherModel())
    }

    override suspend fun getAllFavorites(): Flow<List<RootWeatherModel>> {
        return flowOf(favorites.toList())
    }

    override suspend fun getLastWeather(): Flow<LastWeather> {
        return flowOf(lastWeather ?: LastWeather())
    }

    fun clearALlList(){
        favorites.clear()
    }

    override fun getAllAlerts(): Flow<List<Alerts>> {
        return flowOf(alerts.toList())
    }

    override fun getLanguageFromShdPref(): String {
        return "en"
    }

    override suspend fun getLocationGPS(): Pair<Double, Double> {
        return Pair(0.0, 0.0)
    }

    override suspend fun getLocationMap(): Pair<Double, Double> {
        return Pair(0.0, 0.0)
    }

    override suspend fun insertAlert(alert: Alerts) {
        alerts.add(alert)
    }

    override suspend fun removeAlert(alert: Alerts) {
        alerts.remove(alert)
    }

    override suspend fun updateLastWeather(lastWeather: LastWeather) {
        this.lastWeather = lastWeather
    }

    override suspend fun insertFavorite(favorite: RootWeatherModel) {
        favorites.add(favorite)
    }

    override suspend fun removeFavorite(favorite: RootWeatherModel) {
        favorites.remove(favorite)
    }
}
