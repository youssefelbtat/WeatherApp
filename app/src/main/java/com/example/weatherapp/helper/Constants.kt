package com.example.weatherapp.helper

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.location.Geocoder
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.Toast
import com.example.weatherapp.R
import com.example.weatherapp.data.source.SettingsSharedPreferences
import com.google.android.material.snackbar.Snackbar
import java.util.*

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
    const val NO_INTERNET_MESSAGE = "No internet connection"
    const val TRY_LATER_MESSAGE = "Try Later !"
    const val SH_PRF_LANG_KEY = "language"
    const val SH_PRF_NOTIFICATION_KEY = "notification"
    const val SH_PRF_UNIT_KEY = "unit"
    const val SH_PRF_LOCATION_KEY = "unit"


    fun getCityNameByLatAndLon(context: Context, latitude: Double?, longitude: Double?): String? {
        val lan = SettingsSharedPreferences.getInstance(context).getShPrefLanguage()
        val geocoder = Geocoder(context, Locale(lan))
        val addressList = geocoder.getFromLocation(latitude!!, longitude!!, 1)
        val cityName = if (addressList?.firstOrNull()?.subAdminArea != null)
            addressList.firstOrNull()?.subAdminArea
        else if (addressList?.firstOrNull()?.adminArea != null)
            addressList.firstOrNull()?.adminArea
        else
            addressList?.firstOrNull()?.countryName

        return if (lan == Language.ARABIC.value && !cityName.isNullOrEmpty() && cityName.isEnglish()) {
            addressList?.firstOrNull()?.countryName
        } else {
            cityName
        }
    }

    fun String.isEnglish(): Boolean {
        return this.matches(Regex("[a-zA-Z\\s]+"))
    }



    fun showToast(context: Context,message:String){
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
    fun showSnackBar(rootView: View, message:String){
        Snackbar.make(rootView, message, Snackbar.LENGTH_LONG).show()
    }

    fun isInternetConnected(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = connectivityManager.activeNetwork ?: return false
        val networkCapabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
        return networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }


    fun showDeleteDialog(context: Context, onClick: () -> Unit) {
        val builder = AlertDialog.Builder(context)
        val inflater = LayoutInflater.from(context)
        val dialogView = inflater.inflate(R.layout.alert_dialog, null)

        val buttonDelete: Button = dialogView.findViewById(R.id.btn_delete_item)
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

}



