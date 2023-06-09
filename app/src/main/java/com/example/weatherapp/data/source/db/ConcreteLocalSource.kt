package com.example.weatherapp.data.source.db

import android.content.Context
import com.example.weatherapp.data.model.Alerts
import com.example.weatherapp.data.model.LastWeather
import com.example.weatherapp.data.model.RootWeatherModel
import kotlinx.coroutines.flow.Flow

class ConcreteLocalSource private constructor(private val dao: WeatherDao) : LocalSourceInterface {

    companion object {
        @Volatile private var INSTANCE: ConcreteLocalSource? = null
        fun getInstance(dao: WeatherDao): ConcreteLocalSource {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: ConcreteLocalSource(dao).also { INSTANCE = it }
            }
        }
    }

    override suspend fun getAllFavorites(): Flow<List<RootWeatherModel>> {
       return dao.getAllFavorites()
    }

    override suspend fun insertFavorite(favorite: RootWeatherModel) {
        dao.insertFavorite(favorite)
    }

    override suspend fun removeFavorite(favorite: RootWeatherModel) {
        dao.removeFavorite(favorite)
    }

    override suspend fun getLastWeather(): Flow<LastWeather> {
        return dao.getLastWeather()
    }

    override suspend fun updateLastWeather(lastWeather: LastWeather) {
        dao.updateLastWeather(lastWeather)
    }

    override fun getAllAlerts(): Flow<List<Alerts>> {
        return dao.getAllAlerts()
    }

    override suspend fun insertAlert(alert: Alerts) {
        dao.insertAlert(alert)
    }

    override suspend fun removeAlert(alert: Alerts) {
        dao.removeAlert(alert)
    }
}