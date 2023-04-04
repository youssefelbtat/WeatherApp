package com.example.weatherapp.helper

import android.app.*
import android.content.Context
import android.content.Intent
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import android.os.*
import android.provider.Settings.Secure.getString
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.work.WorkManager
import com.example.weatherapp.R
import com.example.weatherapp.data.model.Alerts
import com.example.weatherapp.data.source.SettingsSharedPreferences
import com.example.weatherapp.ui.MainActivity
import com.example.weatherapp.ui.alert.view.AlarmDialog
import com.google.gson.Gson



class WeatherAlertWorker(
    private val appContext: Context,
    workerParams: WorkerParameters,
) : CoroutineWorker(appContext, workerParams) {

    companion object {
        const val NOTIFICATION_CHANNEL_ID = "weather_alerts"
        const val NOTIFICATION_ID = 123
        const val CHANNEL_NAME = "weather channel"
        const val CHANNEL_DESCRIPTION = "weather channel des"
        const val ALERT_TYPE_WORKER_DATA = "alertType"
        const val CURRENT_WEATHER_WORKER_DATA = "currentWeather"
        const val START_TIME_WORKER_DATA = "startTime"
        const val END_TIME_WORKER_DATA = "endTime"
    }

    private lateinit var alarmDialog: AlarmDialog

    override suspend fun doWork(): Result {
        val alertType = inputData.getString(ALERT_TYPE_WORKER_DATA)
        val alertJson = inputData.getString(CURRENT_WEATHER_WORKER_DATA)
        val currentWeatherAlerts = Gson().fromJson(alertJson, Alerts::class.java)
        val startTime = inputData.getLong(START_TIME_WORKER_DATA, 0L)
        val endTime = inputData.getLong(END_TIME_WORKER_DATA, 0L)

        val currentTime = System.currentTimeMillis() / 1000

        if (currentTime in startTime..endTime) {
            scheduleAlert(currentWeatherAlerts, startTime)
            return Result.success()
        } else {
            WorkManager.getInstance(applicationContext).cancelWorkById(id)
        }
        return Result.failure()

        /*if (currentTime in startTime..endTime) {
            if (alertType == AlertType.NOTIFICATION.name) {
                if (SettingsSharedPreferences.getInstance(appContext).getShPrefNotification()==NotificationEnum.ENABLE.name){
                    createNotification(currentWeatherAlerts)
                }

            } else {
                createAlarm(currentWeatherAlerts)
            }
            return Result.success()
        }else{
            WorkManager.getInstance(applicationContext).cancelWorkById(id)
        }

        return Result.success()*/
    }


    private fun scheduleAlert(alerts: Alerts?, startTime: Long) {
        val intent = Intent(appContext, AlertBroadcastReceiver::class.java).apply {
            putExtra(ALERT_TYPE_WORKER_DATA, inputData.getString(ALERT_TYPE_WORKER_DATA))
            putExtra(CURRENT_WEATHER_WORKER_DATA, inputData.getString(CURRENT_WEATHER_WORKER_DATA))
        }
        val pendingIntent = PendingIntent.getBroadcast(appContext, 0, intent, 0)

        val alarmManager = appContext.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, startTime, pendingIntent)
    }


    fun createAlarm(alerts: Alerts?) {

        Handler(Looper.getMainLooper()).post {
           alarmDialog = AlarmDialog(appContext)
            alarmDialog.setAlarmInfo(setAlertTitleAndDes(alerts).first, setAlertTitleAndDes(alerts).second)
            alarmDialog.showAlarm()
        }


    }

     fun createNotification(alerts: Alerts?) {
        val intent = Intent(appContext, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(appContext, 0, intent, 0)


        val notificationManager =
            appContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel =
                NotificationChannel(
                    NOTIFICATION_CHANNEL_ID,
                    CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_HIGH
                )
            channel.description = CHANNEL_DESCRIPTION
            notificationManager.createNotificationChannel(channel)
        }

        val builder = NotificationCompat.Builder(appContext, NOTIFICATION_CHANNEL_ID)
            .setSmallIcon(R.drawable.notifications)
            .setContentTitle(setAlertTitleAndDes(alerts).first)
            .setContentText(setAlertTitleAndDes(alerts).second)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setFullScreenIntent(pendingIntent, true)
            .setAutoCancel(true)

        val notification = builder.build()
        notificationManager.notify(NOTIFICATION_ID, notification)
    }

    private fun setAlertTitleAndDes(alert:Alerts?):Pair<String,String>{
        val title :String
        val desc :String
        if(alert==null){
            desc=applicationContext.getString(R.string.there_is_no_weather_alerts)
            title=applicationContext.getString(R.string.no_weather_alerts)
        }else{
            title=alert.event ?: applicationContext.getString(R.string.there_is_no_weather_alerts)
            desc=alert.description ?: applicationContext.getString(R.string.there_is_no_weather_alerts)
        }
        return Pair(title,desc)
    }

}
