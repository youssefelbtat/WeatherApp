package com.example.weatherapp.ui.favorite.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.data.model.RootWeatherModel
import com.example.weatherapp.data.repo.RepositoryInterface
import com.example.weatherapp.data.source.network.APIState
import com.example.weatherapp.helper.Constants.APP_ID
import com.example.weatherapp.ui.home.viewmodel.HomeFragmentViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class FavViewModel(
    private val _irepo: RepositoryInterface
) : ViewModel() {
    private var _apiState = MutableStateFlow<APIState<List<RootWeatherModel>>>(APIState.Loading)
    var apiState: StateFlow<APIState<List<RootWeatherModel>>> = _apiState

    init {
       getFavWeathers()
    }

    fun removeFavWeather(weather: RootWeatherModel) {
        viewModelScope.launch(Dispatchers.IO) {
            _irepo.removeFavorite(weather)
            getFavWeathers()
        }
    }
    fun addFavWeather(weather: RootWeatherModel) {
        viewModelScope.launch(Dispatchers.IO) {
            _irepo.insertFavorite(weather)
            getFavWeathers()
        }
    }
    private fun getFavWeathers() = viewModelScope.launch {

        _irepo.getAllFavorites().catch { e ->
            _apiState.value = APIState.Failure(e)
        }.collect() {
            if (it.isEmpty()) {
                _apiState.value=APIState.Empty
            } else {
                _apiState.value = APIState.Success(it)
            }

        }

    }

    suspend fun getWeather(lat:Double, lon:Double): Flow<RootWeatherModel> {
        val lang=_irepo.getLanguageFromShdPref()
        return _irepo.getRootWeatherFromAPI(lat, lon, units = "metric", lang = lang)
    }

}
class FavViewModelFactory(private val _irepo: RepositoryInterface) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(FavViewModel::class.java)) {
            FavViewModel(_irepo) as T
        } else {
            throw IllegalArgumentException("ViewModel class not found")
        }
    }
}