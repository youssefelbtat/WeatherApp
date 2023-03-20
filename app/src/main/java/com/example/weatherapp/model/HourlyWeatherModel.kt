package com.example.weatherapp.model

import com.example.weatherapp.helper.Constants
import com.example.weatherapp.R

data class HourlyWeatherModel(val time:String,val degree:String,val icon:Int)
data class DailyWeatherModel(val day:String,val description:String,val icon:Int,val temp:String,val unit:String)

object getDamyList{
    var mutableList = mutableListOf<HourlyWeatherModel>(
        HourlyWeatherModel("2:00 pm","29\u2103", R.drawable.clear_sky),
        HourlyWeatherModel("3:00 pm","24\u2103", R.drawable.few_clouds),
        HourlyWeatherModel("4:00 pm","26\u2103", R.drawable.thunderstorm),
        HourlyWeatherModel("5:00 pm","25\u2103", R.drawable.broken_cloud),
        HourlyWeatherModel("6:00 pm","24\u2103", R.drawable.snow),
        HourlyWeatherModel("7:00 pm","27\u2103", R.drawable.mist),
        HourlyWeatherModel("8:00 pm","26\u2103", R.drawable.scattered_clouds),
    )
    var dailyWeatherList = mutableListOf<DailyWeatherModel>(
        DailyWeatherModel("Sunday","Clear sky",R.drawable.clear_sky,"26.20/20.5", Constants.Celsius),
        DailyWeatherModel("Saturday","Broken clouds",R.drawable.broken_cloud,"26.20/20.5",
            Constants.Celsius),
        DailyWeatherModel("Monday","Snow",R.drawable.snow,"26.20/20.5", Constants.Celsius),
        DailyWeatherModel("Tuesday","few clouds",R.drawable.few_clouds,"26.20/20.5", Constants.Celsius),
        DailyWeatherModel("Wednesday","scattered clouds",R.drawable.scattered_clouds,"26.20/20.5",
            Constants.Celsius),
        DailyWeatherModel("Thursday","Thunder Storm",R.drawable.thunderstorm,"26.20/20.5",
            Constants.Celsius),
        DailyWeatherModel("Friday","Mist",R.drawable.mist,"26.20/20.5", Constants.Celsius),
    )


}



