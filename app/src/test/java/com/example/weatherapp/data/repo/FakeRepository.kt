package com.example.weatherapp.data.repo

import com.example.weatherapp.data.model.Alerts
import com.example.weatherapp.data.model.LastWeather
import com.example.weatherapp.data.model.RootWeatherModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class FakeRepository : RepositoryInterface{
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
        return flowOf(listOf(RootWeatherModel(), RootWeatherModel()))
    }

    override suspend fun getLastWeather(): Flow<LastWeather> {
        return flowOf(LastWeather())
    }

    override fun getAllAlerts(): Flow<List<Alerts>> {
        return flowOf(listOf(Alerts(), Alerts()))
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
        TODO("Not yet implemented")
    }

    override suspend fun removeAlert(alert: Alerts) {
        TODO("Not yet implemented")
    }
    override suspend fun updateLastWeather(lastWeather: LastWeather) {
        TODO("Not yet implemented")
    }
    override suspend fun insertFavorite(favorite: RootWeatherModel) {
        TODO("Not yet implemented")
    }

    override suspend fun removeFavorite(favorite: RootWeatherModel) {
        TODO("Not yet implemented")
    }

}