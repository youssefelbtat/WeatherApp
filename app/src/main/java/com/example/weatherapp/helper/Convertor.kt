package com.example.weatherapp.helper

import com.example.weatherapp.R
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.time.ZoneId
import java.util.*

object Convertor{
    fun convertDtToDay(dt: Long?): String {
        var dateFormat: DateFormat = SimpleDateFormat("EEEE", Locale.getDefault())
        return dateFormat.format(Date(dt?.times(1000) ?: 0))
    }
    fun convertDtToDate(dt: Long?): String{
        val sdf = SimpleDateFormat("E, dd MMM HH:mm", Locale.getDefault())
        val date = Date(dt?.times(1000) ?: 0)
       return sdf.format(date)
    }
    fun convertDtToTime(dt: Long?): String {
        val sdf = SimpleDateFormat("h:mma", Locale.getDefault())
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