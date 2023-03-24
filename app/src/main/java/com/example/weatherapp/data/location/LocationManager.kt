package com.example.weatherapp.data.location

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import androidx.core.app.ActivityCompat
import com.example.weatherapp.helper.Constants.LOCATION_PERMISSION_ID
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.CompletableDeferred

class LocationManager(private val context: Context) {

    private val fusedLocationProviderClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(context)

    @SuppressLint("MissingPermission")
    suspend fun getLocation(): Pair<Double, Double> {
        val resultDeferred = CompletableDeferred<Pair<Double, Double>>()

        if (checkPermissions()) {
            fusedLocationProviderClient.lastLocation.addOnSuccessListener { location: Location? ->
                if (location != null) {
                    val lat = location.latitude
                    val lon = location.longitude
                    resultDeferred.complete(Pair(lat, lon))
                } else {
                    resultDeferred.completeExceptionally(
                        Exception("Failed to get current location.")
                    )
                }
            }.addOnFailureListener { e: Exception ->
                resultDeferred.completeExceptionally(e)
            }
        } else {
            requestPermissions()

        }

        return resultDeferred.await()
    }

          fun checkPermissions(): Boolean {
            return ActivityCompat.checkSelfPermission(
                context, Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(
                context, Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED

        }

    private fun requestPermissions() {
        ActivityCompat.requestPermissions(
            context as Activity,
            arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            ),
            LOCATION_PERMISSION_ID
        )
    }

}
