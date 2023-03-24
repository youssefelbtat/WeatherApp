package com.example.weatherapp.ui.alert.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weatherapp.data.location.LocationManager
import com.example.weatherapp.data.model.Alerts
import com.example.weatherapp.data.repo.Repository
import com.example.weatherapp.data.source.db.ConcreteLocalSource
import com.example.weatherapp.data.source.network.APIState
import com.example.weatherapp.data.source.network.WeatherApiClient
import com.example.weatherapp.databinding.FragmentAlertBinding
import com.example.weatherapp.ui.alert.viewmodel.AlertViewModel
import com.example.weatherapp.ui.alert.viewmodel.AlertViewModelFactory
import kotlinx.coroutines.flow.collectLatest


class AlertFragment : Fragment() {
    private lateinit var binding: FragmentAlertBinding
    private lateinit var alertFragmentViewModelFactory: AlertViewModelFactory
    private lateinit var viewModel: AlertViewModel
    private lateinit var alertRecyclerAdapter: AlertAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

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
                LocationManager(view.context)
            )
        )
        viewModel = ViewModelProvider(
            this,
            alertFragmentViewModelFactory
        ).get(AlertViewModel::class.java)

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
            viewModel.addAlertWeather(
                Alerts(
                    start = 1020,
                    end = 1025

                )
            )
        }
    }

    private fun setUpRecyclerViews(view: View) {
        alertRecyclerAdapter = AlertAdapter { it ->
            viewModel.removeAlertWeather(it)
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
}

