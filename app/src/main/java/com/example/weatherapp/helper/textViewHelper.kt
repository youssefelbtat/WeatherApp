package com.example.weatherapp.helper

import android.content.Context
import android.widget.TextView
import com.example.weatherapp.R
import com.example.weatherapp.data.source.SettingsSharedPreferences

fun TextView.addTemperature(tem: Double, context: Context) {
    val preferences = SettingsSharedPreferences.getInstance(context)
    val unit = when (preferences.getShPrefUnit()) {
        Units.CELSIUS.name -> Constants.Celsius
        Units.FAHRENHEIT.name -> Constants.Fahrenheit
        Units.KELVIN.name -> Constants.KELVIN
        else -> Constants.Celsius
    }
    val theTemp = convertTemperature(tem, unit)

    text = buildString {
        append("%2.1f".format(theTemp))
        append(unit)
    }
}


fun TextView.addTemperatureMaxAndMin(minTemp: Double, maxTemp: Double, context: Context) {
    val preferences = SettingsSharedPreferences.getInstance(context)
    val unit = when (preferences.getShPrefUnit()) {
        Units.CELSIUS.name -> Constants.Celsius
        Units.FAHRENHEIT.name -> Constants.Fahrenheit
        Units.KELVIN.name -> Constants.KELVIN
        else -> Constants.Celsius
    }

    val minFormatted = convertTemperature(minTemp, unit)
    val maxFormatted = convertTemperature(maxTemp, unit)

    text = buildString {
        append("%.1f/%.1f".format(maxFormatted, minFormatted))
        append(" ")
        append(unit)
    }
}

fun TextView.addWindSpeedInMile(windSpeed:Double,context: Context){
    val unit :String
    val preferences = SettingsSharedPreferences.getInstance(context)
    val speed =when(preferences.getShPrefWindSpeed()){
        WindSpeedEnum.METER.name->{
            unit=resources.getString(R.string.Meter)
            windSpeed*2.23694
        }
        else -> {
            unit=resources.getString(R.string.mile)
            windSpeed
        }
    }
    text= buildString {
        append("%.2f".format(speed))
        append(" ")
        append(unit)
    }
}


fun convertTemperature(tem: Double, targetUnit: String): Double {
    return when (targetUnit) {
        Constants.Celsius -> tem
        Constants.Fahrenheit -> (((tem) * 9.0 / 5) + 32)
        Constants.KELVIN -> (tem) + 273
        else -> tem
    }
}
