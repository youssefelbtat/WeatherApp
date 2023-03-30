package com.example.weatherapp.data.source

import com.example.weatherapp.data.model.RootWeatherModel
import com.example.weatherapp.data.source.network.RemoteWeatherSource

class FakeRemoteWeatherSource : RemoteWeatherSource {
    override suspend fun getWeathersOverNetwork(
        latitude: Double,
        longitude: Double,
        appid: String,
        units: String,
        lang: String
    ): RootWeatherModel {

        return RootWeatherModel(
            lat = latitude,
            lon = longitude,
            )
    }
}
