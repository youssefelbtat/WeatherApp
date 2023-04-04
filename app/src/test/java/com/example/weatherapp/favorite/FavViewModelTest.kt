package com.example.weatherapp.favorite

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.weatherapp.MyMainRule
import com.example.weatherapp.data.model.RootWeatherModel
import com.example.weatherapp.data.repo.FakeRepository
import com.example.weatherapp.data.source.network.APIState
import com.example.weatherapp.ui.favorite.viewmodel.FavViewModel
import junit.framework.TestCase.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4


@RunWith(JUnit4::class)
class FavViewModelTest {

    @get:Rule
    val main = MyMainRule()

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private lateinit var viewModel: FavViewModel
    private lateinit var fakeRepo: FakeRepository

    @Before
    fun setup() {
        fakeRepo = FakeRepository()
        viewModel = FavViewModel(fakeRepo)
    }

    @Test
    fun removeFavWeatherTest_WeatherModel_WeatherIsNotExistingInTheList() = runBlockingTest {
        // given
        val weather1 = RootWeatherModel(id = 105)
        val weather2 = RootWeatherModel(id = 105)
        fakeRepo.insertFavorite(weather1)
        fakeRepo.insertFavorite(weather2)

        // when
        viewModel.removeFavWeather(weather1)

        // then
        when (val favorites = viewModel.apiState.value) {
            is APIState.Success -> {
                assertEquals(false,favorites.data.contains(weather1))
            }
            else -> {
            }
        }
    }

    @Test
    fun addFavWeatherTest_WeatherModel_WeatherIsExistingInTheList() = runBlockingTest {
        // given
        val weather1 = RootWeatherModel(id = 11)
        val weather2 = RootWeatherModel(id = 12)


        // when
        fakeRepo.insertFavorite(weather1)
        fakeRepo.insertFavorite(weather2)
        // then
        when (val favorites = viewModel.apiState.value) {
            is APIState.Success -> {
                assertEquals(true,favorites.data.contains(weather1))
                assertEquals(true,favorites.data.contains(weather2))
            }
            else -> {
            }
        }
    }

    @Test
    fun getFavWeathersOnSuccessTest_returnAllTheFavWeathers() = runBlockingTest {
        // given
        val weather1 = RootWeatherModel(id = 11)
        val weather2 = RootWeatherModel(id = 12)
        //when
        fakeRepo.insertFavorite(weather1)
        fakeRepo.insertFavorite(weather2)

        // Then
        when (val favorites = viewModel.apiState.value)  {
                is APIState.Success -> {
                    // Ensure that all favorite weather items are returned
                    assertEquals(true,favorites.data.contains(weather1))
                    assertEquals(true,favorites.data.contains(weather2))
                }
                else -> {

                }
            }
        }

    @Test
    fun getFavWeathersOnEmptyTest_returnEmptyList() = runBlockingTest {
        //when
        fakeRepo.clearALlFavList()
        delay(100)
        //then

        when ( viewModel.apiState.value)  {
            is APIState.Empty -> {
                assertEquals(0,fakeRepo.getAllFavorites().first().size)
            }
            else -> {

            }
        }
    }



}
