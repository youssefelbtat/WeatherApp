package com.example.weatherapp.home

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.weatherapp.MyMainRule
import com.example.weatherapp.data.model.LastWeather
import com.example.weatherapp.data.model.RootWeatherModel
import com.example.weatherapp.data.repo.FakeRepository
import com.example.weatherapp.data.source.network.APIState
import com.example.weatherapp.ui.favorite.viewmodel.FavViewModel
import com.example.weatherapp.ui.home.viewmodel.HomeFragmentViewModel
import junit.framework.TestCase
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class HomeViewModelTest {
    @get:Rule
    val main = MyMainRule()

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private lateinit var viewModel: HomeFragmentViewModel
    private lateinit var fakeRepo: FakeRepository

    @Before
    fun setup() {
        fakeRepo = FakeRepository()
        viewModel = HomeFragmentViewModel(fakeRepo)
    }

    @Test
    fun testApiStateAfterInitialization() = runBlockingTest {
            val apiState = viewModel.apiState.first()
            assert(apiState is APIState.Loading || apiState is APIState.Success)
        }

/*    @Test
    fun getCurrentWeatherTest_returnWeatherModel() = runBlockingTest {
        fakeRepo.setLocationGPS(Pair(30.30,31.30))
        val currentWeather = viewModel.apiState.value
        println("currentWeather: $currentWeather")
        when (currentWeather) {
            is APIState.Success -> {
                assertEquals(31.30,currentWeather.data.lon)
            }
            else -> {
            }
        }
    }*/


    //Complete the class to test HomeViewModel
}