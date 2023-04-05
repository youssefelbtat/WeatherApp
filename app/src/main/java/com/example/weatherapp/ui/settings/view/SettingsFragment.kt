package com.example.weatherapp.ui.settings.view

import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.preference.PreferenceManager
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.example.weatherapp.R
import com.example.weatherapp.data.source.LocaleHelper
import com.example.weatherapp.data.source.SettingsSharedPreferences
import com.example.weatherapp.databinding.FragmentFavBinding
import com.example.weatherapp.databinding.FragmentSettingsBinding
import com.example.weatherapp.helper.*
import com.example.weatherapp.ui.map.MapsActivity
import kotlinx.coroutines.launch


class SettingsFragment : Fragment() {
    private lateinit var binding: FragmentSettingsBinding
    private lateinit var sharedPreferences: SettingsSharedPreferences


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPreferences = SettingsSharedPreferences.getInstance(requireContext())

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Constants.REQUEST_CODE_MAPS_ACTIVITY_TO_SETTINGS && resultCode == Activity.RESULT_OK) {
            val latitude = data?.getDoubleExtra(Constants.EXTRA_LATITUDE, 0.0)
            val longitude = data?.getDoubleExtra(Constants.EXTRA_LONGITUDE, 0.0)
            if (latitude != null && longitude != null) {
                sharedPreferences.setShPrefLatAndLon(latitude,longitude)
               println("The latitude is $latitude and $longitude")
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initLangGroup()
        initUnitGroup()
        initNotificationGroup()
        initLocationGroup()
        initSpeedGroup()
        binding.rgLang.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.rb_arabic -> {
                    LocaleHelper.setLocale(requireContext(), Language.ARABIC.value)
                    sharedPreferences.setShPrefLanguage(Language.ARABIC)
                }

                R.id.rb_eng -> {
                    LocaleHelper.setLocale(requireContext(), Language.ENGLISH.value)
                    sharedPreferences.setShPrefLanguage(Language.ENGLISH)
                }
            }

            requireActivity().recreate()
        }

        binding.rgWindSpeed.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.rb_meter -> {
                    sharedPreferences.setShPrefWindSpeed(WindSpeedEnum.METER)
                }

                R.id.rb_mile -> {
                    sharedPreferences.setShPrefWindSpeed(WindSpeedEnum.MILE)
                }
            }
        }

        binding.rgUnit.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.rb_celsius -> {
                    sharedPreferences.setShPrefUnit(Units.CELSIUS)
                }

                R.id.rb_Fahrenheit -> {
                    sharedPreferences.setShPrefUnit(Units.FAHRENHEIT)
                }
                R.id.rb_Kelvin -> {
                    sharedPreferences.setShPrefUnit(Units.KELVIN)
                }
            }
        }

        binding.rgNotifications.setOnCheckedChangeListener{ _, checkedId ->
            when (checkedId) {
                R.id.rb_Enable -> {
                    sharedPreferences.setShPrefNotification(NotificationEnum.ENABLE)
                }

                R.id.rb_Desable -> {
                    sharedPreferences.setShPrefNotification(NotificationEnum.DISABLE)
                }

            }
        }

        binding.rgLocation.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.rb_map -> {
                    if (Constants.isInternetConnected(requireContext())){
                        sharedPreferences.setShPrefLocation(LocationEnum.MAP)
                        val intent = Intent(activity, MapsActivity::class.java)
                        startActivityForResult(intent, Constants.REQUEST_CODE_MAPS_ACTIVITY_TO_SETTINGS)
                    }else{
                        Constants.showSnackBar(binding.root, Constants.NO_INTERNET_MESSAGE)
                    }

                }

                R.id.rb_gps -> {
                    sharedPreferences.setShPrefLocation(LocationEnum.GPS)
                }

            }
        }
    }

    private fun initLangGroup() {
        if (sharedPreferences.getShPrefLanguage() == Language.ARABIC.value) {
            binding.rbArabic.isChecked = true
            binding.rbEng.isChecked = false
        } else {
            binding.rbEng.isChecked = true
            binding.rbArabic.isChecked = false
        }
    }

    private fun initSpeedGroup() {
        if (sharedPreferences.getShPrefWindSpeed() == WindSpeedEnum.METER.name) {
            binding.rbMeter.isChecked = true
            binding.rbMile.isChecked = false
        } else {
            binding.rbMeter.isChecked = false
            binding.rbMile.isChecked = true
        }
    }
    private fun initNotificationGroup() {
        if (sharedPreferences.getShPrefNotification() == NotificationEnum.ENABLE.name) {
            binding.rbEnable.isChecked = true
            binding.rbDesable.isChecked = false
        } else {
            binding.rbEnable.isChecked = false
            binding.rbDesable.isChecked = true
        }
    }
    private fun initLocationGroup() {
        if (sharedPreferences.getShPrefLocation() == LocationEnum.GPS.name&&Constants.isInternetConnected(requireContext())) {
            binding.rbGps.isChecked = true
            binding.rbMap.isChecked = false
        } else {
            binding.rbGps.isChecked = false
            binding.rbMap.isChecked = true
        }
    }
    private fun initUnitGroup() {
        if (sharedPreferences.getShPrefUnit() == Units.CELSIUS.name) {
            binding.rbCelsius.isChecked=true
            binding.rbKelvin.isChecked=false
            binding.rbFahrenheit.isChecked=false
        } else if (sharedPreferences.getShPrefUnit() == Units.KELVIN.name) {
            binding.rbCelsius.isChecked=false
            binding.rbKelvin.isChecked=true
            binding.rbFahrenheit.isChecked=false
        } else {
            binding.rbCelsius.isChecked=false
            binding.rbKelvin.isChecked=false
            binding.rbFahrenheit.isChecked=true
        }
    }
}