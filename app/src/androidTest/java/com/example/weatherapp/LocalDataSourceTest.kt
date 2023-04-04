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
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@MediumTest
class LocalDataSourceTest {


    private lateinit var localSource: ConcreteLocalSource
    @get:Rule
    val rule= InstantTaskExecutorRule()

    @get:Rule
    val main =MyMainRule()

    @Before
    fun setUp() {
        localSource = ConcreteLocalSource(ApplicationProvider.getApplicationContext())
    }


    //complete the class

    @Test
    fun insertAndGetAllAlertsTest() = runBlockingTest {
        // Given
        val alert1 = Alerts(1)
        val alert2 = Alerts(2)
        val alert3 = Alerts(3)
        val alert4 = Alerts(4)
        // When
        localSource.insertAlert(alert1)
        localSource.insertAlert(alert2)
        localSource.insertAlert(alert3)

        val alerts = localSource.getAllAlerts().first()
        // Then
        assertEquals(3,alerts.size)
        assertEquals(1,alert1.id)
        assertEquals(2,alert2.id)

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
        val lastWeather = LastWeather(12, 10.0, 12.30)
        localSource.updateLastWeather(lastWeather)
        // When
        val newLastWeather = LastWeather(13, 20.0, 25.60)
        localSource.updateLastWeather(newLastWeather)
        val result = localSource.getLastWeather().first()
        // Then
        assertEquals(13,result.id)
        assertEquals(20.0,result.lat)
        assertEquals(25.60,result.lon)
    }

}


/*
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class ConcreteLocalSourceTest {

    private val context = ApplicationProvider.getApplicationContext<Context>()
    private val localSource = ConcreteLocalSource(context)

    @Test
    fun insertFavoriteTest() = runBlocking {
        // Given
        val favorite = RootWeatherModel(id = 225)

        // When
        localSource.insertFavorite(favorite)

        // Then
        val favorites = localSource.getAllFavorites().first()
        assertEquals(1, favorites.size)
        assertEquals(favorite, favorites[0])
    }

    @Test
    fun removeFavoriteTest() = runBlocking {
        // Given
        val favorite = RootWeatherModel(id = 225)
        localSource.insertFavorite(favorite)

        // When
        localSource.removeFavorite(favorite)

        // Then
        val favorites = localSource.getAllFavorites().first()
        assertEquals(0, favorites.size)
    }

    @Test
    fun updateLastWeatherTest() = runBlocking {
        // Given
        val lastWeather = LastWeather(cityName = "New York", temperature = 20.5)

        // When
        localSource.updateLastWeather(lastWeather)

        // Then
        val result = localSource.getLastWeather().first()
        assertEquals(lastWeather, result)
    }

    @Test
    fun insertAndGetAllAlertsTest() = runBlocking {
        // Given
        val alerts = listOf(
            Alerts("Alert 1", "This is alert 1"),
            Alerts("Alert 2", "This is alert 2")
        )

        // When
        alerts.forEach { localSource.insertAlert(it) }

        // Then
        val result = localSource.getAllAlerts().first()
        assertEquals(alerts, result)
    }

}
*/
