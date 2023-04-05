package com.example.weatherapp.data.source

import android.content.Context
import android.content.SharedPreferences
import android.content.res.Configuration
import android.preference.PreferenceManager
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.weatherapp.helper.*
import com.example.weatherapp.helper.Constants.SH_PRF_LANG_KEY
import com.example.weatherapp.helper.Constants.SH_PRF_LAT_KEY
import com.example.weatherapp.helper.Constants.SH_PRF_LOCATION_KEY
import com.example.weatherapp.helper.Constants.SH_PRF_LON_KEY
import com.example.weatherapp.helper.Constants.SH_PRF_NOTIFICATION_KEY
import com.example.weatherapp.helper.Constants.SH_PRF_SPEED_KEY
import com.example.weatherapp.helper.Constants.SH_PRF_UNIT_KEY
import java.util.*

object LocaleHelper {
    fun setLocale(context: Context, language: String): Context {
        val locale = Locale(language)
        Locale.setDefault(locale)
        val configuration = Configuration(context.resources.configuration)
        configuration.setLocale(locale)
        return context.createConfigurationContext(configuration)
    }
}



 class SettingsSharedPreferences private constructor(context: Context) {

    private val sharedPreferences: SharedPreferences =
        PreferenceManager.getDefaultSharedPreferences(context)

    companion object {
        private var instance: SettingsSharedPreferences? = null

        fun getInstance(context: Context): SettingsSharedPreferences {
            return instance ?: SettingsSharedPreferences(context).also { instance = it }
        }
    }

     fun setShPrefLanguage(lang: Language) {
        sharedPreferences.edit().putString(SH_PRF_LANG_KEY, lang.value).apply()
    }

    fun getShPrefLanguage(): String? {
        return sharedPreferences.getString(SH_PRF_LANG_KEY, Language.ENGLISH.value)
    }

    fun setShPrefUnit(unit: Units) {
        sharedPreferences.edit().putString(SH_PRF_UNIT_KEY, unit.name).apply()
    }

    fun getShPrefUnit(): String? {
        return sharedPreferences.getString(SH_PRF_UNIT_KEY, Units.CELSIUS.name)
    }

    fun setShPrefNotification(state: NotificationEnum) {
        sharedPreferences.edit().putString(SH_PRF_NOTIFICATION_KEY, state.name).apply()
    }

    fun getShPrefNotification(): String? {
        return sharedPreferences.getString(SH_PRF_NOTIFICATION_KEY, NotificationEnum.DISABLE.name)
    }

    fun setShPrefLocation(location: LocationEnum) {
        sharedPreferences.edit().putString(SH_PRF_LOCATION_KEY, location.name).apply()
    }

    fun getShPrefLocation(): String? {
        return sharedPreferences.getString(SH_PRF_LOCATION_KEY, LocationEnum.GPS.name)
    }

     fun setShPrefWindSpeed(speed: WindSpeedEnum) {
         sharedPreferences.edit().putString(SH_PRF_SPEED_KEY, speed.name).apply()
     }

     fun getShPrefWindSpeed(): String? {
         return sharedPreferences.getString(SH_PRF_SPEED_KEY, WindSpeedEnum.METER.name)
     }

     fun setShPrefLatAndLon(lat:Double,lon:Double) {
         sharedPreferences.edit().putFloat(SH_PRF_LAT_KEY, lat.toFloat()).apply()
         sharedPreferences.edit().putFloat(SH_PRF_LON_KEY, lon.toFloat()).apply()
     }

     fun getShPrefLatAndLon(): Pair<Double,Double>? {
         return Pair(sharedPreferences.getFloat(SH_PRF_LAT_KEY, 0.0f).toDouble(),sharedPreferences.getFloat(SH_PRF_LON_KEY, 0.0f).toDouble())
     }
}

