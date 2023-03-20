package com.example.weatherapp.model

import kotlinx.coroutines.flow.Flow

interface RepositoryInterface {
    suspend fun  getRootWeather(latitude: Double,
                                longitude: Double,
                                appid: String,
                                units: String,
                                lang: String): Flow<RootWeatherModel>
}