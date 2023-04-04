package com.example.weatherapp.alert

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.weatherapp.MyMainRule
import com.example.weatherapp.data.model.Alerts
import com.example.weatherapp.data.repo.FakeRepository
import com.example.weatherapp.data.source.network.APIState
import com.example.weatherapp.ui.alert.viewmodel.AlertViewModel
import junit.framework.TestCase
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4



@RunWith(JUnit4::class)
class AlertViewModelTest {

    @get:Rule
    val main = MyMainRule()

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private lateinit var viewModel: AlertViewModel
    private lateinit var fakeRepo: FakeRepository

    @Before
    fun setup() {
        fakeRepo = FakeRepository()
        viewModel = AlertViewModel(fakeRepo)
    }

    @Test
    fun removeAlertTest_AlertModel_AlertIsNotExistingInTheList() = runBlockingTest {
        // given
        val alert1 = Alerts(id = 105)
        val alert2 = Alerts(id = 106)
        fakeRepo.insertAlert(alert2)
        fakeRepo.insertAlert(alert1)

        // when
        viewModel.removeAlertWeather(alert1)

        // then
        when (val alerts = viewModel.apiState.value) {
            is APIState.Success -> {
                TestCase.assertEquals(false,alerts.data.contains(alert1))
            }
            else -> {
            }
        }
    }

    @Test
    fun addAlertTest_AlertModel_AlertIsExistingInTheList() = runBlockingTest {
        // given
        val alert1 = Alerts(id = 105)
        val alert2 = Alerts(id = 106)


        // when
        fakeRepo.insertAlert(alert2)
        fakeRepo.insertAlert(alert1)
        // then
        when (val alerts = viewModel.apiState.value) {
            is APIState.Success -> {
                TestCase.assertEquals(true, alerts.data.contains(alert1))
                TestCase.assertEquals(true, alerts.data.contains(alert2))
            }
            else -> {
            }
        }
    }

    @Test
    fun getAllAlertsOnSuccessTest_returnAllTheAllAlerts() = runBlockingTest {
        // given
        val alert1 = Alerts(id = 105)
        val alert2 = Alerts(id = 106)
        //when
        fakeRepo.insertAlert(alert2)
        fakeRepo.insertAlert(alert1)

        // Then
        when (val allAlertsList = viewModel.apiState.value)  {
            is APIState.Success -> {
                // Ensure that all favorite weather items are returned
                TestCase.assertEquals(true, allAlertsList.data.contains(alert1))
                TestCase.assertEquals(true, allAlertsList.data.contains(alert2))
            }
            else -> {

            }
        }
    }

    @Test
    fun getAllAlertsOnEmptyTest_returnEmptyList() = runBlockingTest {
        //when
        fakeRepo.clearALlAlertsList()
        delay(100)
        //then

        when ( viewModel.apiState.value)  {
            is APIState.Empty -> {
                // Ensure that all favorite weather items are returned
                TestCase.assertEquals(0, fakeRepo.getAllAlerts().first().size)
            }
            else -> {

            }
        }
    }


}