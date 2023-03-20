package com.example.weatherapp.network

import com.example.weatherapp.model.RootWeatherModel
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