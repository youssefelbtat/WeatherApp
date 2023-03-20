package com.example.weatherapp.model

import com.google.gson.annotations.SerializedName


data class RootWeatherModel(

    @SerializedName("lat") var lat: Double? = null,
    @SerializedName("lon") var lon: Double? = null,
    @SerializedName("timezone") var timezone: String? = null,
    @SerializedName("timezone_offset") var timezoneOffset: Int? = null,
    @SerializedName("current") var current: Current? = Current(),
    @SerializedName("hourly") var hourly: ArrayList<Hourly> = arrayListOf(),
    @SerializedName("daily") var daily: ArrayList<Daily> = arrayListOf()

)

data class Weather(

    @SerializedName("id") var id: Int? = null,
    @SerializedName("main") var main: String? = null,
    @SerializedName("description") var description: String? = null,
    @SerializedName("icon") var icon: String? = null

)

data class Current(

    @SerializedName("dt") var dt: Long? = null,
    @SerializedName("sunrise") var sunrise: Int? = null,
    @SerializedName("sunset") var sunset: Int? = null,
    @SerializedName("temp") var temp: Double? = null,
    @SerializedName("feels_like") var feelsLike: Double? = null,
    @SerializedName("pressure") var pressure: Int? = null,
    @SerializedName("humidity") var humidity: Int? = null,
    @SerializedName("dew_point") var dewPoint: Double? = null,
    @SerializedName("uvi") var uvi: Double? = null,
    @SerializedName("clouds") var clouds: Int? = null,
    @SerializedName("visibility") var visibility: Int? = null,
    @SerializedName("wind_speed") var windSpeed: Double? = null,
    @SerializedName("wind_deg") var windDeg: Int? = null,
    @SerializedName("wind_gust") var windGust: Double? = null,
    @SerializedName("weather") var weather: ArrayList<Weather> = arrayListOf()

)

data class Hourly(

    @SerializedName("dt") var dt: Long? = null,
    @SerializedName("temp") var temp: Double? = null,
    @SerializedName("feels_like") var feelsLike: Double? = null,
    @SerializedName("pressure") var pressure: Int? = null,
    @SerializedName("humidity") var humidity: Int? = null,
    @SerializedName("dew_point") var dewPoint: Double? = null,
    @SerializedName("uvi") var uvi: Double? = null,
    @SerializedName("clouds") var clouds: Int? = null,
    @SerializedName("visibility") var visibility: Int? = null,
    @SerializedName("wind_speed") var windSpeed: Double? = null,
    @SerializedName("wind_deg") var windDeg: Int? = null,
    @SerializedName("wind_gust") var windGust: Double? = null,
    @SerializedName("weather") var weather: ArrayList<Weather> = arrayListOf(),
    @SerializedName("pop") var pop: Double? = null

)

data class Daily(

    @SerializedName("dt") var dt: Long? = null,
    @SerializedName("sunrise") var sunrise: Int? = null,
    @SerializedName("sunset") var sunset: Int? = null,
    @SerializedName("moonrise") var moonrise: Int? = null,
    @SerializedName("moonset") var moonset: Int? = null,
    @SerializedName("moon_phase") var moonPhase: Double? = null,
    @SerializedName("temp") var temp: Temp? = Temp(),
    @SerializedName("feels_like") var feelsLike: FeelsLike? = FeelsLike(),
    @SerializedName("pressure") var pressure: Int? = null,
    @SerializedName("humidity") var humidity: Int? = null,
    @SerializedName("dew_point") var dewPoint: Double? = null,
    @SerializedName("wind_speed") var windSpeed: Double? = null,
    @SerializedName("wind_deg") var windDeg: Int? = null,
    @SerializedName("wind_gust") var windGust: Double? = null,
    @SerializedName("weather") var weather: ArrayList<Weather> = arrayListOf(),
    @SerializedName("clouds") var clouds: Int? = null,
    @SerializedName("pop") var pop: Double? = null,
    @SerializedName("uvi") var uvi: Double? = null

)

data class FeelsLike(

    @SerializedName("day") var day: Double? = null,
    @SerializedName("night") var night: Double? = null,
    @SerializedName("eve") var eve: Double? = null,
    @SerializedName("morn") var morn: Double? = null

)

data class Temp(

    @SerializedName("day") var day: Double? = null,
    @SerializedName("min") var min: Double? = null,
    @SerializedName("max") var max: Double? = null,
    @SerializedName("night") var night: Double? = null,
    @SerializedName("eve") var eve: Double? = null,
    @SerializedName("morn") var morn: Double? = null

)