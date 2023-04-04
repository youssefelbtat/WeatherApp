package com.example.weatherapp

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.filters.SmallTest
import com.example.weatherapp.data.model.Alerts
import com.example.weatherapp.data.model.LastWeather
import com.example.weatherapp.data.model.RootWeatherModel
import com.example.weatherapp.data.source.db.WeatherDao
import com.example.weatherapp.data.source.db.WeatherDatabase
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@ExperimentalCoroutinesApi
@RunWith(JUnit4::class)
@SmallTest
class WeatherDaoTest {


    @get:Rule
    val main = MyMainRule()

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private lateinit var db: WeatherDatabase
    private lateinit var dao: WeatherDao

    @Before
    fun setUp() {
        db = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            WeatherDatabase::class.java
        ).build()

        dao = db.weatherDao()
    }

    @After
    fun closeDB() {
        db.close()
    }

    @Test
    fun insertFavWeatherTest_WeatherModel_ExistingInAllFavorites() = runBlockingTest {
        //Given
        val favorite = RootWeatherModel(id = 225)
        //When
        dao.insertFavorite(favorite)
        //Then
        assertEquals(true, dao.getAllFavorites().first().contains(favorite))
    }

    @Test
    fun removeFavWeatherTest_WeatherModel_DoesNotExistingInAllFavorites() = runBlockingTest {
        //Given
        val favorite = RootWeatherModel(id = 225)
        dao.insertFavorite(favorite)
        assertEquals(true, dao.getAllFavorites().first().contains(favorite))
        //When
        dao.removeFavorite(favorite)
        //Then
        assertEquals(false, dao.getAllFavorites().first().contains(favorite))

    }

    @Test
    fun getAllFavWeatherTest__returnALlTheFavWeathers() = runBlockingTest {
        //When
        val favorite = RootWeatherModel(id = 225)
        val favorite1 = RootWeatherModel(id = 226)
        val favorite2 = RootWeatherModel(id = 227)
        dao.insertFavorite(favorite)
        dao.insertFavorite(favorite1)
        dao.insertFavorite(favorite2)

        //Then
        assertEquals(3, dao.getAllFavorites().first().size)
    }

    @Test
    fun updateLastWeatherTest_WeatherModel_changeTheLastWeather() = runBlockingTest {
        //Given
        val lastWeather = LastWeather(id = 203)

        // When
        dao.updateLastWeather(lastWeather)

        // Then
       assertEquals(203,dao.getLastWeather().first().id)
    }

    @Test
    fun getLastWeatherTest__returnTheLastWeather() = runBlockingTest {
        //Given
        val lastWeather = LastWeather(id = 203)

        // When
        dao.updateLastWeather(lastWeather)

        // Then
        assertEquals(203,dao.getLastWeather().first().id)
    }

    @Test
    fun insertAlertTest_AlertModel_ExistingInAllAlerts() = runBlockingTest {
        //Given
        val alert = Alerts(id = 30)
        val alert1 = Alerts(id = 31)
        val alert2 = Alerts(id = 32)
        //When
        dao.insertAlert(alert)
        dao.insertAlert(alert1)
        dao.insertAlert(alert2)

        // Then
        assertEquals(true,dao.getAllAlerts().first().contains(alert))
        assertEquals(true,dao.getAllAlerts().first().contains(alert1))
        assertEquals(true,dao.getAllAlerts().first().contains(alert2))
    }

    @Test
    fun removeAlertTest_AlertModel_NotExistingInAllAlerts() = runBlockingTest {
        //Given
        val alert = Alerts(id = 30)
        val alert1 = Alerts(id = 31)
        val alert2 = Alerts(id = 32)
        dao.insertAlert(alert)
        dao.insertAlert(alert1)
        dao.insertAlert(alert2)
        //When
        dao.removeAlert(alert)
        dao.removeAlert(alert1)
        dao.removeAlert(alert2)

        // Then
        assertEquals(false,dao.getAllAlerts().first().contains(alert))
        assertEquals(false,dao.getAllAlerts().first().contains(alert1))
        assertEquals(false,dao.getAllAlerts().first().contains(alert2))
    }

    @Test
    fun getAllAlertsTest__returnALlTheAlerts() = runBlockingTest {
        //When
        val alert = Alerts(id = 225)
        val alert1 = Alerts(id = 226)
        val alert2 = Alerts(id = 227)
        dao.insertAlert(alert)
        dao.insertAlert(alert1)
        dao.insertAlert(alert2)

        //Then
        assertEquals(3, dao.getAllAlerts().first().size)
    }

}

