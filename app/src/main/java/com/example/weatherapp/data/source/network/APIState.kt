package com.example.weatherapp.data.source.network

import com.example.weatherapp.data.model.RootWeatherModel

sealed class APIState<out T> {
        class Success<out T>(val data: T) : APIState<T>()
        class Failure(val msg: Throwable) : APIState<Nothing>()
        object Loading : APIState<Nothing>()
        object Empty : APIState<Nothing>()
}
