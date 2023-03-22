package com.example.weatherapp.data.source.network

import com.example.weatherapp.data.model.RootWeatherModel

sealed class APIState {
        class Success(val data: RootWeatherModel): APIState()
        class Failure(val msg: Throwable): APIState()
        object Loading : APIState()

}