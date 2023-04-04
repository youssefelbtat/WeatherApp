package com.example.weatherapp.data.source

import android.content.Context
import android.content.SharedPreferences
import android.content.res.Configuration
import android.preference.PreferenceManager
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.weatherapp.helper.Constants.SH_PRF_LANG_KEY
import com.example.weatherapp.helper.Constants.SH_PRF_LOCATION_KEY
import com.example.weatherapp.helper.Constants.SH_PRF_NOTIFICATION_KEY
import com.example.weatherapp.helper.Constants.SH_PRF_UNIT_KEY
import com.example.weatherapp.helper.Language
import com.example.weatherapp.helper.LocationEnum
import com.example.weatherapp.helper.NotificationEnum
import com.example.weatherapp.helper.Units
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
}

