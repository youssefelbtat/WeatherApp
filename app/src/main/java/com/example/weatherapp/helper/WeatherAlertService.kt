package com.example.weatherapp.helper

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Handler
import android.os.Looper
import androidx.core.app.NotificationCompat
import com.example.weatherapp.R
import com.example.weatherapp.data.model.Alerts
import com.example.weatherapp.data.source.SettingsSharedPreferences
import com.example.weatherapp.ui.MainActivity
import com.example.weatherapp.ui.alert.view.AlarmDialog
import com.google.gson.Gson

class AlertBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        println("....AlertBroadcastReceiver Start....")
        // extract the alert data from the intent and show the notification or alarm dialog
        val alertType = intent?.getStringExtra(WeatherAlertWorker.ALERT_TYPE_WORKER_DATA)
        val alertJson = intent?.getStringExtra(WeatherAlertWorker.CURRENT_WEATHER_WORKER_DATA)
        val currentWeatherAlerts = Gson().fromJson(alertJson, Alerts::class.java)
        if (alertType == AlertType.NOTIFICATION.name) {
            if (SettingsSharedPreferences.getInstance(context!!).getShPrefNotification()==NotificationEnum.ENABLE.name){
                createNotification(context, currentWeatherAlerts)
            }
        } else {
            println("AlertBroadcastReceiver create Alarm")
            createAlarm(context, currentWeatherAlerts)
        }
    }

    private fun createAlarm(context: Context?, alerts: Alerts?) {
        lateinit var alarmDialog: AlarmDialog

        Handler(Looper.getMainLooper()).post {
            alarmDialog = AlarmDialog(context!!)
            alarmDialog.setAlarmInfo(setAlertTitleAndDes(context,alerts).first, setAlertTitleAndDes(context,alerts).second)
            alarmDialog.showAlarm()
        }


    }

    private fun createNotification(context: Context?, alerts: Alerts?) {
        val notificationManager =
            context?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel =
                NotificationChannel(
                    WeatherAlertWorker.NOTIFICATION_CHANNEL_ID,
                    WeatherAlertWorker.CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_HIGH
                )
            channel.description = WeatherAlertWorker.CHANNEL_DESCRIPTION
            notificationManager.createNotificationChannel(channel)
        }

        val builder = NotificationCompat.Builder(context,
            WeatherAlertWorker.NOTIFICATION_CHANNEL_ID
        )
            .setSmallIcon(R.drawable.notifications)
            .setContentTitle(setAlertTitleAndDes(context,alerts).first)
            .setContentText(setAlertTitleAndDes(context,alerts).second)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)

        val notification = builder.build()
        notificationManager.notify(WeatherAlertWorker.NOTIFICATION_ID, notification)
    }

    private fun setAlertTitleAndDes(context: Context?,alert:Alerts?):Pair<String,String>{
        val title :String
        val desc :String
        if(alert==null){
            desc= context?.applicationContext?.getString(R.string.there_is_no_weather_alerts).toString()
            title =context?.applicationContext?.getString(R.string.no_weather_alerts).toString()
        }else{
            title=alert.event ?: context?.applicationContext?.getString(R.string.no_weather_alerts).toString()
            desc=alert.description ?: context?.applicationContext?.getString(R.string.there_is_no_weather_alerts).toString()
        }
        return Pair(title,desc)
    }

}
