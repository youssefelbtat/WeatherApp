package com.example.weatherapp.data.source.db

import androidx.room.TypeConverter
import com.example.weatherapp.data.model.Alerts
import com.example.weatherapp.data.model.Current
import com.example.weatherapp.data.model.Daily
import com.example.weatherapp.data.model.Hourly
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class DailyTypeConverter {
    @TypeConverter
    fun fromString(value: String): ArrayList<Daily> {
        val listType = object : TypeToken<ArrayList<Daily>>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun fromArrayList(list: ArrayList<Daily>): String {
        val gson = Gson()
        return gson.toJson(list)
    }
}


class AlertsTypeConvertor{
    @TypeConverter
    fun fromString(value: String): ArrayList<Alerts> {
        val listType = object : TypeToken<ArrayList<Alerts>>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun fromArrayList(list: ArrayList<Alerts>): String {
        val gson = Gson()
        return gson.toJson(list)
    }
}

class HourlyTypeConverter {
    @TypeConverter
    fun fromString(value: String): ArrayList<Hourly> {
        val listType = object : TypeToken<ArrayList<Hourly>>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun fromArrayList(list: ArrayList<Hourly>): String {
        val gson = Gson()
        return gson.toJson(list)
    }
}

class CurrentConverter {
    @TypeConverter
    fun fromCurrent(current: Current?): String? {
        return Gson().toJson(current)
    }

    @TypeConverter
    fun toCurrent(currentJson: String?): Current? {
        return Gson().fromJson(currentJson, Current::class.java)
    }
}

class StringListConverter {
    @TypeConverter
    fun fromStringList(stringList: List<String>?): String? {
        return Gson().toJson(stringList)
    }

    @TypeConverter
    fun toStringList(stringListJson: String?): List<String>? {
        val type = object : TypeToken<List<String>?>() {}.type
        return Gson().fromJson(stringListJson, type)
    }
}

