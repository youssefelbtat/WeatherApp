package com.example.weatherapp.data.repo

import android.content.Context
import com.example.weatherapp.data.location.LocationManager
import com.example.weatherapp.data.model.Alerts
import com.example.weatherapp.data.model.LastWeather
import com.example.weatherapp.data.model.RootWeatherModel
import com.example.weatherapp.data.source.db.LocalSourceInterface
import com.example.weatherapp.data.source.network.RemoteWeatherSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class Repository private constructor(
    var remoteSource: RemoteWeatherSource,
    var localSource: LocalSourceInterface,
    var locationManager: LocationManager
) : RepositoryInterface {
    companion object {
        private const val TAG = "Repository"
        private var instance: Repository? = null
        fun getInstance(
            remoteSource: RemoteWeatherSource,
            localSource: LocalSourceInterface,
            locationManager: LocationManager
        ): Repository {
            return instance ?: synchronized(this) {
                val temp = Repository(
                    remoteSource,
                    localSource,
                    locationManager
                )
                instance = temp
                temp

            }
        }
    }

    init {
        this.remoteSource = remoteSource
        this.localSource= localSource
        this.locationManager=locationManager
    }

    override suspend fun getRootWeatherFromAPI(
        latitude: Double,
        longitude: Double,
        appid: String,
        units: String,
        lang: String
    ): Flow<RootWeatherModel> {
        return flowOf(remoteSource.getWeathersOverNetwork(latitude, longitude, appid, units, lang))
    }

    override suspend fun getAllFavorites(): Flow<List<RootWeatherModel>> {
        return localSource.getAllFavorites()
    }

    override suspend fun insertFavorite(favorite: RootWeatherModel) {
        localSource.insertFavorite(favorite)
    }

    override suspend fun removeFavorite(favorite: RootWeatherModel) {
        localSource.removeFavorite(favorite)
    }

    override suspend fun getLastWeather(): Flow<LastWeather>{
        return localSource.getLastWeather()
    }

    override suspend fun updateLastWeather(lastWeather: LastWeather) {
        return localSource.updateLastWeather(lastWeather)
    }

    override fun getAllAlerts(): Flow<List<Alerts>> {
       return localSource.getAllAlerts()
    }

    override suspend fun insertAlert(alert: Alerts) {
        localSource.insertAlert(alert)
    }

    override suspend fun removeAlert(alert: Alerts) {
        localSource.removeAlert(alert)
    }

    override suspend fun getLocationGPS(): Pair<Double, Double> {
      return locationManager.getLocation()
    }

    override suspend fun getLocationMap(): Pair<Double, Double> {
        TODO("Not yet implemented")
    }


}