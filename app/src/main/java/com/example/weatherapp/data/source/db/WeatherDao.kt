package com.example.weatherapp.data.source.db

import androidx.room.Dao
import androidx.room.*
import androidx.room.Query
import com.example.weatherapp.data.model.Alerts
import com.example.weatherapp.data.model.LastWeather
import com.example.weatherapp.data.model.RootWeatherModel
import kotlinx.coroutines.flow.Flow

@Dao
interface WeatherDao {
    @Query("SELECT * FROM favorites_table")
    fun getAllFavorites(): Flow<List<RootWeatherModel>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavorite(favorite: RootWeatherModel)

    @Delete
    suspend fun removeFavorite(favorite: RootWeatherModel)

    @Query("SELECT * FROM last_weather_table LIMIT 1")
    fun getLastWeather(): Flow<LastWeather>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateLastWeather(lastWeather: LastWeather)

    @Query("SELECT * FROM alerts_table")
    fun getAllAlerts(): Flow<List<Alerts>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAlert(alert: Alerts)

    @Delete
    suspend fun removeAlert(alert: Alerts)
}