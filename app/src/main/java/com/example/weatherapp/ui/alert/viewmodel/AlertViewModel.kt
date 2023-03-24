package com.example.weatherapp.ui.alert.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.data.model.Alerts
import com.example.weatherapp.data.model.RootWeatherModel
import com.example.weatherapp.data.repo.RepositoryInterface
import com.example.weatherapp.data.source.network.APIState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class AlertViewModel(
    private val _irepo: RepositoryInterface
) : ViewModel() {
    private var _apiState = MutableStateFlow<APIState<List<Alerts>>>(APIState.Loading)
    var apiState: StateFlow<APIState<List<Alerts>>> = _apiState

    init {
        getAlertWeathers()
    }

    fun removeAlertWeather(alert: Alerts) {
        viewModelScope.launch(Dispatchers.IO) {
            _irepo.removeAlert(alert)
            getAlertWeathers()
        }
    }
    fun addAlertWeather(alert: Alerts) {
        viewModelScope.launch(Dispatchers.IO) {
            _irepo.insertAlert(alert)
            getAlertWeathers()
        }
    }
    private fun getAlertWeathers() = viewModelScope.launch {

        _irepo.getAllAlerts().catch { e ->
            _apiState.value = APIState.Failure(e)
        }.collect() {
            if (it.isEmpty()) {
                _apiState.value= APIState.Empty
            } else {
                _apiState.value = APIState.Success(it)
            }

        }

    }
}
class AlertViewModelFactory(private val _irepo: RepositoryInterface) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(AlertViewModel::class.java)) {
            AlertViewModel(_irepo) as T
        } else {
            throw IllegalArgumentException("ViewModel class not found")
        }
    }
}