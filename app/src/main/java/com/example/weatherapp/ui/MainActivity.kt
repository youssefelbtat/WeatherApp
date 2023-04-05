package com.example.weatherapp.ui

import android.content.Context
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.weatherapp.R
import com.example.weatherapp.data.source.LocaleHelper
import com.example.weatherapp.data.source.SettingsSharedPreferences
import com.example.weatherapp.databinding.ActivityMainBinding
import com.example.weatherapp.helper.Constants
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private lateinit var binding: ActivityMainBinding
    private val drawerLayout by lazy { binding.drawerLayout }
    private val navController by lazy { findNavController(R.id.nav_host_fragment) }
    private val actionBar by lazy { supportActionBar!! }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if (!Constants.isInternetConnected(this))
            Constants.showSnackBar(binding.root, Constants.NO_INTERNET_MESSAGE)

        actionBar.apply {
            title = getString(R.string.home)
            setHomeAsUpIndicator(R.drawable.menu_icon)
            setDisplayHomeAsUpEnabled(true)
        }

        binding.NavigationView.apply {
            setupWithNavController(navController)
            setNavigationItemSelectedListener(this@MainActivity)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        navController.navigate(item.itemId)
        actionBar.title = item.title
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    drawerLayout.closeDrawer(GravityCompat.START)
                } else {
                    drawerLayout.openDrawer(GravityCompat.START)
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
    override fun attachBaseContext(newBase: Context) {
        val sharedPrefs = SettingsSharedPreferences.getInstance(context = newBase)
        val language = sharedPrefs.getShPrefLanguage()
        val context = language?.let { LocaleHelper.setLocale(newBase, it) }
        super.attachBaseContext(context)
    }

}
