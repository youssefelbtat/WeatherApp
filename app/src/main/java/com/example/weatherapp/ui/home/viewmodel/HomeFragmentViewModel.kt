package com.example.weatherapp.ui.home.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.helper.Constants.APP_ID
import com.example.weatherapp.model.RepositoryInterface
import com.example.weatherapp.network.APIState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class HomeFragmentViewModel(
    private val _irepo: RepositoryInterface
) : ViewModel() {
    private var _apiState = MutableStateFlow<APIState>(APIState.Loading)
    var apiState : StateFlow<APIState> = _apiState

    init {
        getCurrentWeather()
    }
    private fun getCurrentWeather() = viewModelScope.launch{
        _irepo.getRootWeather(31.5555379,31.075331,APP_ID,"metric","en").catch {e->
            _apiState.value =APIState.Failure(e)
        }.collect(){
            _apiState.value=APIState.Success(it)
        }

    }

}

class HomeViewModelFactory (private val _irepo: RepositoryInterface): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if(modelClass.isAssignableFrom(HomeFragmentViewModel::class.java)){
            HomeFragmentViewModel(_irepo) as T
        }else{
            throw IllegalArgumentException("ViewModel class not found")
        }
    }
}