package com.example.weatherapp.data.source.network

import com.example.weatherapp.data.model.RootWeatherModel
import retrofit2.Response

interface RemoteWeatherSource {
    suspend fun getWeathersOverNetwork(
        latitude: Double,
        longitude: Double,
        appid: String,
        units: String,
        lang: String
    ): RootWeatherModel

}