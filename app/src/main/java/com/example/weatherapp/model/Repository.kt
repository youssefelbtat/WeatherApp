package com.example.weatherapp.model

import com.example.weatherapp.network.RemoteWeatherSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class Repository private constructor(
    var remoteSource: RemoteWeatherSource,
) : RepositoryInterface {
    companion object {
        private const val TAG = "Repository"
        private var instance: Repository? = null
        fun getInstance(
            remoteSource: RemoteWeatherSource,
        ): Repository {
            return instance ?: synchronized(this) {
                val temp = Repository(
                    remoteSource
                )
                instance = temp
                temp

            }
        }
    }

    init {
        this.remoteSource = remoteSource
    }

    override suspend fun getRootWeather(
        latitude: Double,
        longitude: Double,
        appid: String,
        units: String,
        lang: String
    ): Flow<RootWeatherModel> {
        return flowOf(remoteSource.getWeathersOverNetwork(latitude, longitude, appid, units, lang))
    }


}