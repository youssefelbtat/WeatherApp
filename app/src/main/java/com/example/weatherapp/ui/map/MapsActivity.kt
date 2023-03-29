package com.example.weatherapp.ui.map

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.weatherapp.R
import com.example.weatherapp.databinding.ActivityMapsBinding
import com.example.weatherapp.helper.Constants.EXTRA_LATITUDE
import com.example.weatherapp.helper.Constants.EXTRA_LONGITUDE
import com.example.weatherapp.helper.Constants.NO_INTERNET_MESSAGE
import com.example.weatherapp.helper.Constants.isInternetConnected
import com.example.weatherapp.helper.Constants.showSnackBar
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import java.util.*


class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private lateinit var selectedPlaceLatLng: LatLng

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        binding.btnSave.setOnClickListener {
            if (isInternetConnected(this)){
                val resultIntent = Intent().apply {
                    putExtra(EXTRA_LATITUDE, selectedPlaceLatLng.latitude)
                    putExtra(EXTRA_LONGITUDE, selectedPlaceLatLng.longitude)
                }
                setResult(Activity.RESULT_OK, resultIntent)
                finish()
            }else{
                showSnackBar(binding.root, NO_INTERNET_MESSAGE)
            }

        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        mMap.setOnCameraMoveListener {
            selectedPlaceLatLng = mMap.cameraPosition.target
            mMap.clear()
            mMap.addMarker(MarkerOptions().position(selectedPlaceLatLng))
        }

        val sydney = LatLng(-34.0, 151.0)
        mMap.addMarker(MarkerOptions().position(sydney).title(" "))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
        setMapLongClick(googleMap)
    }
    private fun setMapLongClick(map: GoogleMap) {
        map.setOnMapClickListener { latLng ->
            map?.clear()
            val snippet = String.format(
                Locale.getDefault(), "Lat: %1$.5f, Long: %2$.5f", latLng.latitude, latLng.longitude
            )
            val markerOptions = MarkerOptions().position(latLng).snippet(snippet)
            map.moveCamera(
                CameraUpdateFactory.newLatLng(
                    latLng
                )
            )
            map.addMarker(markerOptions)


        }
        map.setOnMapLongClickListener { latLng ->
        }

    }
}