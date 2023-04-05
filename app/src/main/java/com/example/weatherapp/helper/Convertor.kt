package com.example.weatherapp.helper

import android.content.Context
import com.example.weatherapp.R
import com.example.weatherapp.data.source.SettingsSharedPreferences
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.time.ZoneId
import java.util.*

object Convertor{
    fun convertDtToDay(context:Context,dt: Long?): String {
        val lan = SettingsSharedPreferences.getInstance(context).getShPrefLanguage()
        var dateFormat: DateFormat = SimpleDateFormat("EEEE", Locale(lan))
        return dateFormat.format(Date(dt?.times(1000) ?: 0))
    }
    fun convertDtToDate(context:Context,dt: Long?): String{
        val lan = SettingsSharedPreferences.getInstance(context).getShPrefLanguage()
        val sdf = SimpleDateFormat("E, dd MMM HH:mm", Locale(lan))
        val date = Date(dt?.times(1000) ?: 0)
       return sdf.format(date)
    }
    fun convertDtToNumberDate(context:Context,dt: Long?): String{
        val lan = SettingsSharedPreferences.getInstance(context).getShPrefLanguage()
        val sdf = SimpleDateFormat("dd/MM/yyyy", Locale(lan))
        val date = Date(dt?.times(1000) ?: 0)
        return sdf.format(date)
    }
    fun convertDtToTime(context:Context,dt: Long?): String {
        val lan = SettingsSharedPreferences.getInstance(context).getShPrefLanguage()
        val sdf = SimpleDateFormat("h:mma", Locale(lan))
        sdf.timeZone = TimeZone.getDefault()
        return sdf.format(dt?.times(1000) ?: 0)
    }


    fun convertIconToDrawableImage(icon: String?): Int {
        val drawableImage: Int = when (icon?.take(2)) {
            "01" -> R.drawable.clear_sky
            "02" -> R.drawable.few_clouds
            "03" -> R.drawable.scattered_clouds
            "04" -> R.drawable.broken_cloud
            "09" -> R.drawable.shower_rain
            "10" -> R.drawable.rain
            "11" -> R.drawable.thunderstorm
            "13" -> R.drawable.snow
            "50" -> R.drawable.mist
            else -> R.drawable.clear_sky
        }
        return drawableImage
    }
}