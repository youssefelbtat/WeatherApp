package com.example.weatherapp.data.source.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.weatherapp.data.model.Alerts
import com.example.weatherapp.data.model.LastWeather
import com.example.weatherapp.data.model.RootWeatherModel

@Database(entities = [RootWeatherModel::class, LastWeather::class, Alerts::class], version = 5)
@TypeConverters(AlertsTypeConvertor::class,DailyTypeConverter::class, HourlyTypeConverter::class,CurrentConverter::class,StringListConverter::class)
abstract class WeatherDatabase : RoomDatabase() {
    abstract fun weatherDao(): WeatherDao

    companion object {
        private var instance: WeatherDatabase? = null

        fun getInstance(context: Context): WeatherDatabase {
            return instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder(
                    context.applicationContext,
                    WeatherDatabase::class.java,
                    "weather_database"
                ).fallbackToDestructiveMigration().build().also { instance = it }
            }
        }
    }
}
