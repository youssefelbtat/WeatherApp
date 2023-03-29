package com.example.weatherapp.data.repo

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.weatherapp.data.location.LocationManager
import com.example.weatherapp.data.model.Alerts
import com.example.weatherapp.data.model.LastWeather
import com.example.weatherapp.data.model.RootWeatherModel
import com.example.weatherapp.data.source.FakeLocalSource
import com.example.weatherapp.data.source.FakeRemoteWeatherSource
import com.example.weatherapp.data.source.SettingsSharedPreferences
import com.example.weatherapp.helper.Constants
import com.example.weatherapp.helper.Language
import junit.framework.TestCase.*
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith



@RunWith(AndroidJUnit4::class)
class RepositoryTest {
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var repository: RepositoryInterface

    @Before
    fun setup() {
       // val context = ApplicationProvider.getApplicationContext<Context>()
        val fakeRemoteSource = FakeRemoteWeatherSource()
        val fakeLocalSource = FakeLocalSource()
        val locationManager = LocationManager(ApplicationProvider.getApplicationContext())
        val settingsSharedPreferences = SettingsSharedPreferences.getInstance(ApplicationProvider.getApplicationContext())
        repository = Repository.getInstance(
            fakeRemoteSource,
            fakeLocalSource,
            locationManager,
            settingsSharedPreferences
        )
    }

    @Test
    fun getRootWeatherFromAPITest_latitudeAndLongitude_returnsRootWeatherModel() = runBlockingTest {
        // given
        val latitude = 36.665
        val longitude = 36.150
        val appid = Constants.APP_ID
        val expected = RootWeatherModel(lat = latitude, lon = longitude)

        // when
        val result = repository.getRootWeatherFromAPI(latitude, longitude, appid).first()

        // then
        assertEquals(expected, result)
    }

    @Test
    fun getAllFavoritesTest_returnsListOfRootWeatherModel() = runBlockingTest {
        // given
        repository.insertFavorite(RootWeatherModel(lat = 45.50))
        repository.insertFavorite(RootWeatherModel(lat = 33.50))
        repository.insertFavorite(RootWeatherModel(lat = 11.30))

        // when
        val result = repository.getAllFavorites().first()

        // then
        assertEquals(3, result.size)
        assertEquals(45.50, result[0].lat)
        assertEquals(33.50, result[1].lat)
        assertEquals(11.30, result[2].lat)
    }

    @Test
    fun getLastWeatherTest_returnsLastWeather() = runBlockingTest {
        // given
        val expected = LastWeather()

        // when
        val result = repository.getLastWeather().first()

        // then
        assertEquals(expected, result)
    }

    @Test
    fun updateLastWeatherTest_callUpdateLastWeather_() = runBlockingTest {
        // given
        val lastWeather = LastWeather(lat = 30.60)

        // when
        repository.updateLastWeather(lastWeather)

        // then
        assertEquals(30.60,repository.getLastWeather().first().lat)
        repository.updateLastWeather(LastWeather(lat = 65.30))
        assertEquals(65.30,repository.getLastWeather().first().lat)
    }

    @Test
    fun getAllAlertsTest_returnsListOfAlerts() = runBlockingTest {
        // given
        repository.insertAlert(Alerts(start = 100))
        repository.insertAlert(Alerts(start = 200))
        repository.insertAlert(Alerts(start = 300))

        // when
        val result = repository.getAllAlerts().first()

        // then
        assertEquals(3, result.size)
        assertEquals(100.toLong(), result[0].start)
        assertEquals(200.toLong(), result[1].start)
        assertEquals(300.toLong(), result[2].start)
    }

    @Test
    fun getLanguageFromShdPrefTest_returnsString() {

        //when
        val result = repository.getLanguageFromShdPref()

        //then
        assertEquals(Language.ENGLISH.value, result)

    }

    @Test
    fun insertFavoriteTest_RootWeatherModel_IncreaseTheFavoriteList() = runBlockingTest {
        // given
        val favorite = RootWeatherModel()
        val favListSize=repository.getAllFavorites().first().size

        // when
        repository.insertFavorite(favorite)

        // then
        assertEquals(repository.getAllFavorites().first().size, favListSize + 1)
    }

    @Test
    fun insertAlertTest_AlertModel_IncreaseTheAlertsList()= runBlockingTest{
        // given
        val alert = Alerts()
        val alertsListSize=repository.getAllAlerts().first().size

        // when
        repository.insertAlert(alert)

        // then
        assertEquals(repository.getAllAlerts().first().size, alertsListSize + 1)
    }

}
