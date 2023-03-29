package com.example.weatherapp.ui.alert.view

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.RadioGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weatherapp.R
import com.example.weatherapp.data.location.LocationManager
import com.example.weatherapp.data.model.Alerts
import com.example.weatherapp.data.repo.Repository
import com.example.weatherapp.data.source.SettingsSharedPreferences
import com.example.weatherapp.data.source.db.ConcreteLocalSource
import com.example.weatherapp.data.source.network.APIState
import com.example.weatherapp.data.source.network.WeatherApiClient
import com.example.weatherapp.databinding.FragmentAlertBinding
import com.example.weatherapp.helper.Constants.showDeleteDialog
import com.example.weatherapp.ui.alert.viewmodel.AlertViewModel
import com.example.weatherapp.ui.alert.viewmodel.AlertViewModelFactory
import kotlinx.coroutines.flow.collectLatest


class AlertFragment : Fragment() {
    private lateinit var binding: FragmentAlertBinding
    private lateinit var alertFragmentViewModelFactory: AlertViewModelFactory
    private lateinit var viewModel: AlertViewModel
    private lateinit var alertRecyclerAdapter: AlertAdapter


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
                ConcreteLocalSource(view.context),
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
            showAddAlertDialog(requireContext()){
                viewModel.addAlertWeather(
                    Alerts(
                        start = 1020,
                        end = 1025
                    )
                )
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

    private fun showAddAlertDialog(context: Context, onClick: () -> Unit) {
        val builder = AlertDialog.Builder(context)
        val inflater = LayoutInflater.from(context)
        val dialogView = inflater.inflate(R.layout.set_alert_dialog, null)

        val btnSave: Button = dialogView.findViewById(R.id.btn_save_alert)
        val btnCancel: Button = dialogView.findViewById(R.id.btn_cancel_alert)
        val edtStartDate :EditText = dialogView.findViewById(R.id.edt_start_date)
        val edtEndDate :EditText = dialogView.findViewById(R.id.edt_end_date)
        val edtStartTime :EditText = dialogView.findViewById(R.id.edt_start_time)
        val edtEndTime :EditText = dialogView.findViewById(R.id.edt_end_time)
        val alertType :RadioGroup =dialogView.findViewById(R.id.radioGroup)

        builder.setView(dialogView)

        val dialog = builder.create()

        edtStartDate.setOnClickListener {
            //show date picker and get date from the user
        }

        btnSave.setOnClickListener {
            onClick.invoke()
            dialog.dismiss()

        }

        btnCancel.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }
}

