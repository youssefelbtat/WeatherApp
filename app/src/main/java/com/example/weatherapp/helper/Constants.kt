package com.example.weatherapp.helper

import android.content.Context
import android.location.Geocoder

object Constants {
    const val Celsius = "\u2103"
    const val Fahrenheit = "\u2109"
    const val Kelvin = "\u212A"
    const val BASE_URL = "https://api.openweathermap.org/data/2.5/"
    const val APP_ID = "5105a7173c3805fa7994a304fe55b5ea"

    fun getCityNameByLatAndLon(context: Context, latitude: Double?, longitude: Double?): String? {
        val geocoder = Geocoder(context)
        val addressList = geocoder.getFromLocation(latitude!!, longitude!!, 1)
        return addressList?.firstOrNull()?.adminArea
    }




}