package com.example.weatherapp.ui.alert.viewmodel

import android.content.Context
import androidx.lifecycle.*
import androidx.work.*
import com.example.weatherapp.data.model.Alerts
import com.example.weatherapp.data.repo.RepositoryInterface
import com.example.weatherapp.data.source.network.APIState
import com.example.weatherapp.helper.WeatherAlertWorker
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class AlertViewModel(
    private val _repo: RepositoryInterface,
) : ViewModel() {
    private var _apiState = MutableStateFlow<APIState<List<Alerts>>>(APIState.Loading)
    var apiState: StateFlow<APIState<List<Alerts>>> = _apiState

    init {
        getAlertWeathers()
    }
    private val _notificationSent = MutableLiveData<Boolean>()
    val notificationSent: LiveData<Boolean>
        get() = _notificationSent

    private fun sendNotification(alert: Alerts, context: Context) {
        viewModelScope.launch {
            _repo.sendNotification(alert,context)
            _notificationSent.value = true
        }
    }

    fun removeAlertWeather(alert: Alerts) {
        viewModelScope.launch(Dispatchers.IO) {
            _repo.removeAlert(alert)
            getAlertWeathers()
        }
    }
    fun addAlertWeather(alert: Alerts,context: Context) {
        viewModelScope.launch(Dispatchers.IO) {
            _repo.insertAlert(alert)
            sendNotification(alert,context)
            println("The current : "+System.currentTimeMillis()/1000)
            getAlertWeathers()
        }
    }
    private fun getAlertWeathers() = viewModelScope.launch {

        _repo.getAllAlerts().catch { e ->
            _apiState.value = APIState.Failure(e)
        }.collect() {
            if (it.isEmpty()) {
                _apiState.value= APIState.Empty
            } else {
                _apiState.value = APIState.Success(it)
            }

        }

    }

/*
    suspend fun schudAlert(){
         val workManager = WorkManager.getInstance(context)
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val inputData = Data.Builder()
            .putLong("start", _repo.getAllAlerts().first().get(0).start!!)
            .putLong("end", _repo.getAllAlerts().first().get(0).end!!)
            .build()

        val workRequest = PeriodicWorkRequestBuilder<WeatherAlertWorker>(
            1, TimeUnit.HOURS,
            flexTimeInterval = 10, TimeUnit.MINUTES
        )
            .setConstraints(constraints)
            .setInputData(inputData)
            .build()

        workManager.enqueue(workRequest)
    }
*/


}
/*val WeaterWorkRequest = OneTimeWorkRequest.Builder(WeatherAlertWorker::class.java)
            .setConstraints(constraints)
            .build()*/
class AlertViewModelFactory(private val _irepo: RepositoryInterface) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(AlertViewModel::class.java)) {
            AlertViewModel(_irepo) as T
        } else {
            throw IllegalArgumentException("ViewModel class not found")
        }
    }
}