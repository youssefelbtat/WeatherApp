package com.example.weatherapp

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.example.weatherapp.data.model.Alerts
import com.example.weatherapp.data.model.LastWeather
import com.example.weatherapp.data.model.RootWeatherModel
import com.example.weatherapp.data.source.db.ConcreteLocalSource
import com.example.weatherapp.data.source.db.WeatherDao
import com.example.weatherapp.data.source.db.WeatherDatabase
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@MediumTest
class LocalDataSourceTest {


    @get:Rule
    val rule= InstantTaskExecutorRule()

    @get:Rule
    val main =MyMainRule()

    private lateinit var localSource: ConcreteLocalSource
    private lateinit var db:WeatherDatabase


    @Before
    fun setUp() {
        db=Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            WeatherDatabase::class.java
        ).allowMainThreadQueries().build()

        localSource = ConcreteLocalSource.getInstance(db.weatherDao())
    }
    @After
    fun downTear(){
        db.close()
    }


    @Test
    fun insertAndGetAllAlertsTest_oneOrMoreAlert_IncreaseTheSizeAllAlerts() = runBlockingTest {
        // Given
        val alert1 = Alerts(1)
        val alert2 = Alerts(2)
        val alert3 = Alerts(3)
        val alert4 = Alerts(4)
        // When
        localSource.insertAlert(alert1)
        localSource.insertAlert(alert2)

        // Then
        assertEquals(2,localSource.getAllAlerts().first().size)
        assertEquals(1,alert1.id)
        assertEquals(2,alert2.id)
        localSource.insertAlert(alert3)
        localSource.insertAlert(alert4)
        assertEquals(4,localSource.getAllAlerts().first().size)
        assertEquals(3,alert3.id)
        assertEquals(4,alert4.id)

    }

    @Test
    fun insertAndGetAllFavTest_oneOrMoreWeatherModel_IncreaseTheSizeAllFavorites() = runBlockingTest {
        // Given
        val weather1 = RootWeatherModel(1)
        val weather2 = RootWeatherModel(2)
        val weather3 = RootWeatherModel(3)
        val weather4 = RootWeatherModel(4)

        // When
        localSource.insertFavorite(weather1)
        localSource.insertFavorite(weather2)

        // Then
        assertEquals(2,localSource.getAllFavorites().first().size)
        assertEquals(1,weather1.id)
        assertEquals(2,weather2.id)
        localSource.insertFavorite(weather3)
        localSource.insertFavorite(weather4)
        assertEquals(4,localSource.getAllFavorites().first().size)
        assertEquals(3,weather3.id)
        assertEquals(4,weather4.id)

    }

    @Test
    fun removeFavoriteTest_WeatherModel_ModelNotExistingInAllFavorites() = runBlockingTest {
        // Given
        val favorite = RootWeatherModel(id = 15)
        localSource.insertFavorite(favorite)
        assertEquals(1,localSource.getAllFavorites().first().size)
        // When
        localSource.removeFavorite(favorite)
        // Then
        assertEquals(0,localSource.getAllFavorites().first().size)
    }

    @Test
    fun removeAlertTest_WeatherModel_ModelNotExistingInAllAlerts() = runBlockingTest {
        // Given
        val alert = Alerts(id = 15)
        localSource.insertAlert(alert)
        assertEquals(1,localSource.getAllFavorites().first().size)
        // When
        localSource.removeAlert(alert)
        // Then
        assertEquals(0,localSource.getAllAlerts().first().size)
    }

    @Test
    fun getLastWeatherTest__returnLastWeather() = runBlockingTest {
        // Given
        val lastWeather = LastWeather(13, lat = 30.5, lon = 31.50)
        localSource.updateLastWeather(lastWeather)
        // When
        val result = localSource.getLastWeather().first()
        // Then
        assertEquals(13,result.id)
        assertEquals(30.5,result.lat)
        assertEquals(31.50,result.lon)

    }

    @Test
    fun updateLastWeatherTest_WeatherModel_ChangeLastWeather() = runBlockingTest {
        // Given
        val newLastWeather = LastWeather(lat = 15.50, lon = 6.23)
        localSource.updateLastWeather(newLastWeather)
        localSource.updateLastWeather(LastWeather(lat = 30.65, lon = 66.30))

        // When
        val result = localSource.getLastWeather().first()
        // Then
        assertEquals(30.65,result.lat)
        assertEquals(66.30,result.lon)
    }

}
