package com.example.weatherapp.data.repo

import android.content.Context
import com.example.weatherapp.data.model.*
import com.example.weatherapp.helper.LocationEnum
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class FakeRepository : RepositoryInterface {
     private val favorites = mutableListOf<RootWeatherModel>()
    private var lastWeather: LastWeather? = null
    private val alerts = mutableListOf<Alerts>()
    private lateinit var gpsLocation :Pair<Double,Double>

    override suspend fun getRootWeatherFromAPI(
        latitude: Double,
        longitude: Double,
        appid: String,
        units: String,
        lang: String
    ): Flow<RootWeatherModel> {
        val rootWeatherModel = RootWeatherModel(
            lat = gpsLocation.first,
            lon = gpsLocation.second,
            timezone = "GMT",
            current = Current(),
            daily = arrayListOf(Daily()),
            hourly = arrayListOf(Hourly()),
            timezoneOffset = 0
        )
        return flowOf(rootWeatherModel)
    }


    override suspend fun getAllFavorites(): Flow<List<RootWeatherModel>> {
        return flowOf(favorites.toList())
    }

    override suspend fun getLastWeather(): Flow<LastWeather> {
        return flowOf(lastWeather ?: LastWeather())
    }

    fun clearALlFavList(){
        favorites.clear()
    }
    fun clearALlAlertsList(){
        alerts.clear()
    }

    override fun getAllAlerts(): Flow<List<Alerts>> {
        return flowOf(alerts.toList())
    }

    override fun getLanguageFromShdPref(): String {
        return "en"
    }

    override fun getLocationTypeShdPref(): String {
        return LocationEnum.GPS.name
    }

    override suspend fun sendNotification(alert: Alerts, context: Context) {
        TODO("Not yet implemented")
    }

    override suspend fun getLocationGPS(): Pair<Double, Double> {
        return gpsLocation
    }

    fun setLocationGPS(cord: Pair<Double,Double>){
        gpsLocation=cord
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
