package com.example.weatherapp.helper

import android.app.AlertDialog
import android.content.Context
import android.location.Geocoder
import android.view.LayoutInflater
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.example.weatherapp.R

object Constants {
    const val Celsius = "\u2103"
    const val Fahrenheit = "\u2109"
    const val KELVIN = "\u212A"
    const val BASE_URL = "https://api.openweathermap.org/data/2.5/"
    const val APP_ID = "5105a7173c3805fa7994a304fe55b5ea"
    const val LOCATION_PERMISSION_ID = 21
    const val REQUEST_CODE_MAPS_ACTIVITY_TO_FAV = 100
    const val EXTRA_LATITUDE = "latitude"
    const val EXTRA_LONGITUDE = "longitude"

    fun getCityNameByLatAndLon(context: Context, latitude: Double?, longitude: Double?): String? {
        val geocoder = Geocoder(context)
        val addressList = geocoder.getFromLocation(latitude!!, longitude!!, 1)
        return if (addressList?.firstOrNull()?.locality != null)
            addressList?.firstOrNull()?.locality
        else if (addressList?.firstOrNull()?.subAdminArea != null)
            addressList?.firstOrNull()?.subAdminArea
        else if (addressList?.firstOrNull()?.adminArea != null)
            addressList?.firstOrNull()?.adminArea
        else
            addressList?.firstOrNull()?.countryName
    }
    fun showDialog(context: Context, onClick: () -> Unit) {
        val builder = AlertDialog.Builder(context)
        val inflater = LayoutInflater.from(context)
        val dialogView = inflater.inflate(R.layout.alert_dialog, null)

        val textViewDeleteConfirmation: TextView = dialogView.findViewById(R.id.textViewDeleteConfirmation)
        val buttonDelete: Button = dialogView.findViewById(R.id.buttonDelete)
        val buttonCancel: Button = dialogView.findViewById(R.id.buttonCancel)

        builder.setView(dialogView)

        val dialog = builder.create()

        buttonDelete.setOnClickListener {
            onClick.invoke()
            dialog.dismiss()

        }

        buttonCancel.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    fun showToast(context: Context,message:String){
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

}



