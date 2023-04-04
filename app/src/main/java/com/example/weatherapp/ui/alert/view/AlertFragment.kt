package com.example.weatherapp.ui.alert.view

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.icu.text.SimpleDateFormat
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.RadioGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weatherapp.R
import com.example.weatherapp.data.location.LocationManager
import com.example.weatherapp.data.model.Alerts
import com.example.weatherapp.data.repo.Repository
import com.example.weatherapp.data.source.SettingsSharedPreferences
import com.example.weatherapp.data.source.db.ConcreteLocalSource
import com.example.weatherapp.data.source.db.WeatherDatabase
import com.example.weatherapp.data.source.network.APIState
import com.example.weatherapp.data.source.network.WeatherApiClient
import com.example.weatherapp.databinding.FragmentAlertBinding
import com.example.weatherapp.helper.AlertType
import com.example.weatherapp.helper.Constants
import com.example.weatherapp.helper.Constants.showDeleteDialog
import com.example.weatherapp.helper.LocationEnum
import com.example.weatherapp.ui.alert.viewmodel.AlertViewModel
import com.example.weatherapp.ui.alert.viewmodel.AlertViewModelFactory
import com.example.weatherapp.ui.map.MapsActivity
import kotlinx.coroutines.flow.collectLatest
import java.util.*
import kotlin.collections.ArrayList


class AlertFragment : Fragment() {
    private lateinit var binding: FragmentAlertBinding
    private lateinit var alertFragmentViewModelFactory: AlertViewModelFactory
    private lateinit var viewModel: AlertViewModel
    private lateinit var alertRecyclerAdapter: AlertAdapter
    private  val REQUEST_CODE_SYSTEM_ALERT_WINDOW_PERMISSION = 100


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAlertBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        alertFragmentViewModelFactory = AlertViewModelFactory(
            Repository.getInstance(
                WeatherApiClient.getInstance(),
                ConcreteLocalSource.getInstance(WeatherDatabase.getInstance(requireContext()).weatherDao()),
                LocationManager(view.context),
                SettingsSharedPreferences.getInstance(view.context)
            )
        )
        viewModel = ViewModelProvider(
            this,
            alertFragmentViewModelFactory
        )[AlertViewModel::class.java]

        setUpRecyclerViews(view)

        lifecycleScope.launchWhenCreated {
            viewModel.apiState.collectLatest { result ->
                when (result) {
                    is APIState.Empty -> showEmpty()
                    is APIState.Success -> {
                        showList()
                        alertRecyclerAdapter.submitList(result.data)
                    }
                    else -> {

                    }
                }
            }
        }
        binding.fabAddAlert.setOnClickListener {
            if (Constants.isInternetConnected(requireContext())){
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(context)) {
                    // If the system alert window permission is not granted, request it
                    val intent = Intent(
                        Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:${context?.packageName}")
                    )
                    startActivityForResult(intent, REQUEST_CODE_SYSTEM_ALERT_WINDOW_PERMISSION)
                } else {
                    // Permission is granted, add the window
                    showAddAlertDialog(requireContext()){
                        viewModel.addAlertWeather(it,requireContext())
                    }
                }


            }else{
                Constants.showSnackBar(binding.root, Constants.NO_INTERNET_MESSAGE)
            }


        }
        viewModel.notificationSent.observe(viewLifecycleOwner) {
            if (it) {
                println("The notification is $it")
            }
        }
    }

    private fun setUpRecyclerViews(view: View) {
        alertRecyclerAdapter = AlertAdapter { it ->
            showDeleteDialog(view.context) {
                viewModel.removeAlertWeather(it)
            }
        }

        binding.rvAlerts.apply {
            adapter = alertRecyclerAdapter.apply {
                submitList(ArrayList())
            }
            setHasFixedSize(false)
            layoutManager = LinearLayoutManager(context)
        }
    }

    private fun showEmpty() {
        println("On Empty:")
        binding.emptyAlertGroup.visibility = View.VISIBLE
        binding.rvAlerts.visibility = View.GONE
    }

    private fun showList() {
        println("On Success:")
        binding.emptyAlertGroup.visibility = View.GONE
        binding.rvAlerts.visibility = View.VISIBLE
    }

    private fun showAddAlertDialog(context: Context, onClick: (alert:Alerts) -> Unit) {
        val builder = AlertDialog.Builder(context)
        val inflater = LayoutInflater.from(context)
        val dialogView = inflater.inflate(R.layout.set_alert_dialog, null)
        val alertModel  = Alerts()
        val btnSave: Button = dialogView.findViewById(R.id.btn_save_alert)
        val btnCancel: Button = dialogView.findViewById(R.id.btn_cancel_alert)
        val edtStartDate :EditText = dialogView.findViewById(R.id.edt_start_date)
        val edtEndDate :EditText = dialogView.findViewById(R.id.edt_end_date)
        val edtStartTime :EditText = dialogView.findViewById(R.id.edt_start_time)
        val edtEndTime :EditText = dialogView.findViewById(R.id.edt_end_time)
        val alertType :RadioGroup =dialogView.findViewById(R.id.radioGroup)
        val cals = Calendar.getInstance()
        val cald = Calendar.getInstance()


        alertType.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.rb_notification -> {
                    alertModel.type=AlertType.NOTIFICATION.name
                }

                R.id.rb_alerm -> {
                    alertModel.type=AlertType.ALARM.name
                }

            }
        }

        alertModel.type

        builder.setView(dialogView)

        val dialog = builder.create()

        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val timeFormat = SimpleDateFormat("h:mm a", Locale.getDefault())

        edtStartDate.setOnClickListener {
            val now = Calendar.getInstance()
            val datePicker = DatePickerDialog(
                context,
                { _, year, month, dayOfMonth ->
                    cals.set(Calendar.YEAR, year)
                    cals.set(Calendar.MONTH, month)
                    cals.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                    edtStartDate.setText(dateFormat.format(cals.time))
                },
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH)
            )
            datePicker.show()
        }

        edtEndDate.setOnClickListener {
            val now = Calendar.getInstance()
            val datePicker = DatePickerDialog(
                context,
                { _, year, month, dayOfMonth ->
                    cald.set(Calendar.YEAR, year)
                    cald.set(Calendar.MONTH, month)
                    cald.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                    edtEndDate.setText(dateFormat.format(cald.time))
                },
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH)
            )
            datePicker.show()
        }

        edtStartTime.setOnClickListener {
            val now = Calendar.getInstance()
            val timePicker = TimePickerDialog(
                context,
                { _, hourOfDay, minute ->

                    cals.set(Calendar.HOUR_OF_DAY, hourOfDay)
                    cals.set(Calendar.MINUTE, minute)
                    edtStartTime.setText(timeFormat.format(cals.time))
                },
                now.get(Calendar.HOUR_OF_DAY),
                now.get(Calendar.MINUTE),
                false
            )
            timePicker.show()
        }

        edtEndTime.setOnClickListener {
            val now = Calendar.getInstance()
            val timePicker = TimePickerDialog(
                context,
                { _, hourOfDay, minute ->

                    cald.set(Calendar.HOUR_OF_DAY, hourOfDay)
                    cald.set(Calendar.MINUTE, minute)
                    edtEndTime.setText(timeFormat.format(cald.time))
                },
                now.get(Calendar.HOUR_OF_DAY),
                now.get(Calendar.MINUTE),
                false
            )
            timePicker.show()
        }

        btnSave.setOnClickListener {

            if (Constants.isInternetConnected(requireContext())){
                alertModel.end=cald.timeInMillis /1000
                alertModel.start=cals.timeInMillis /1000
                onClick.invoke(alertModel)
                dialog.dismiss()
            }else{
                Constants.showSnackBar(binding.root, Constants.NO_INTERNET_MESSAGE)
            }

        }

        btnCancel.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }
}

