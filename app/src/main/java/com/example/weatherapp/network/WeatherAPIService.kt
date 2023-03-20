package com.example.weatherapp.network

import com.example.weatherapp.model.RootWeatherModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherAPIService {
    @GET("onecall")
    suspend fun getRootWeather(
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double,
        @Query("appid") appid: String,
        @Query("units") units: String,
        @Query("lang") lang: String
    ): RootWeatherModel

}