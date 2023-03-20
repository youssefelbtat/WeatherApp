package com.example.weatherapp.network

import com.example.weatherapp.model.RootWeatherModel

sealed class APIState {
        class Success(val data: RootWeatherModel):APIState()
        class Failure(val msg: Throwable):APIState()
        object Loading : APIState()

}