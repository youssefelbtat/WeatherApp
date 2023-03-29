package com.example.weatherapp.ui.home.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.data.model.LastWeather
import com.example.weatherapp.data.model.RootWeatherModel
import com.example.weatherapp.data.repo.RepositoryInterface
import com.example.weatherapp.data.source.SettingsSharedPreferences
import com.example.weatherapp.data.source.network.APIState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import java.lang.String.join

class HomeFragmentViewModel(
    private val _irepo: RepositoryInterface
) : ViewModel() {
    private var _apiState = MutableStateFlow<APIState<RootWeatherModel>>(APIState.Loading)
    var apiState: StateFlow<APIState<RootWeatherModel>> = _apiState

    init {
        getCurrentWeather()
    }

    private fun getCurrentWeather() = viewModelScope.launch {
        try {
            val (latitude, longitude) = _irepo.getLocationGPS()
            val lang=_irepo.getLanguageFrom()
            _irepo.getRootWeatherFromAPI(latitude = latitude, longitude =longitude, units = "metric", lang = lang)
                .collect() {
                    _apiState.value = APIState.Success(it)
                    _irepo.updateLastWeather(
                        LastWeather(
                            lat = it.lat,
                            lon = it.lon,
                            timezone = it.timezone,
                            current = it.current,
                            daily = it.daily,
                            hourly = it.hourly,
                            timezoneOffset = it.timezoneOffset
                        )
                    )

                }
        } catch (e: Exception) {
            val lastWeather = _irepo.getLastWeather().firstOrNull()
            if (lastWeather != null) {
                _apiState.value = APIState.Loading
                _apiState.value = APIState.Success(RootWeatherModel(
                    lat = lastWeather.lat,
                    lon = lastWeather.lon,
                    timezone = lastWeather.timezone,
                    current = lastWeather.current,
                    daily = lastWeather.daily,
                    hourly = lastWeather.hourly,
                    timezoneOffset = lastWeather.timezoneOffset
                ))
            } else {
                _apiState.value = APIState.Failure(e)
            }

        }

    }

}

class HomeViewModelFactory(private val _irepo: RepositoryInterface) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(HomeFragmentViewModel::class.java)) {
            HomeFragmentViewModel(_irepo) as T
        } else {
            throw IllegalArgumentException("ViewModel class not found")
        }
    }
}