package com.example.weatherapp.data.source.network

import com.example.weatherapp.data.model.RootWeatherModel

class WeatherApiClient private constructor() : RemoteWeatherSource {
    private val weatherAPIService : WeatherAPIService by lazy {
        RetrofitHelper.getInstance().create(WeatherAPIService::class.java)
    }


    companion object {
        private var instance: WeatherApiClient?=null
        fun getInstance(): WeatherApiClient {
            return instance ?: synchronized(this){
                val temp = WeatherApiClient()
                instance = temp
                temp
            }
        }
    }

    override suspend fun getWeathersOverNetwork(
        latitude: Double,
        longitude: Double,
        appid: String,
        units: String,
        lang: String
    ): RootWeatherModel {
        return weatherAPIService.getRootWeather(
            latitude,
            longitude,
            appid,
            units,
            lang
        )
    }


}