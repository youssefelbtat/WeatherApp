package com.example.weatherapp.ui.settings.view

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.preference.PreferenceManager
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.weatherapp.R
import com.example.weatherapp.data.source.LocaleHelper
import com.example.weatherapp.data.source.SettingsSharedPreferences
import com.example.weatherapp.databinding.FragmentFavBinding
import com.example.weatherapp.databinding.FragmentSettingsBinding
import com.example.weatherapp.helper.Language
import com.example.weatherapp.helper.Units


class SettingsFragment : Fragment() {
    private lateinit var binding: FragmentSettingsBinding
    private lateinit var sharedPreferences: SettingsSharedPreferences


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPreferences = SettingsSharedPreferences.getInstance(requireContext())

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