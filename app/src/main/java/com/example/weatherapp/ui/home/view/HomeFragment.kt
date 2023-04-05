package com.example.weatherapp.ui.home.view

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.data.location.LocationManager
import com.example.weatherapp.data.model.RootWeatherModel
import com.example.weatherapp.databinding.FragmentHomeBinding
import com.example.weatherapp.helper.Constants
import com.example.weatherapp.helper.Convertor
import com.example.weatherapp.data.repo.Repository
import com.example.weatherapp.data.source.SettingsSharedPreferences
import com.example.weatherapp.data.source.db.ConcreteLocalSource
import com.example.weatherapp.data.source.db.WeatherDao
import com.example.weatherapp.data.source.db.WeatherDatabase
import com.example.weatherapp.data.source.network.APIState
import com.example.weatherapp.data.source.network.WeatherApiClient
import com.example.weatherapp.helper.Constants.LOCATION_PERMISSION_ID
import com.example.weatherapp.helper.Constants.isInternetConnected
import com.example.weatherapp.helper.addTemperature
import com.example.weatherapp.helper.addWindSpeedInMile
import com.example.weatherapp.ui.home.viewmodel.HomeFragmentViewModel
import com.example.weatherapp.ui.home.viewmodel.HomeViewModelFactory
import com.example.weatherapp.ui.map.MapsActivity
import kotlinx.coroutines.flow.collectLatest

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var homeFragmentViewModelFactory: HomeViewModelFactory
    private lateinit var viewModel: HomeFragmentViewModel
    private lateinit var hourlyRecyclerAdapter: HourlyWeatherAdabter
    private lateinit var dailyRecyclerAdapter: DailyWeatherAdabter


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    @Deprecated("Deprecated in Java")
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_PERMISSION_ID) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                lifecycleScope.launchWhenCreated {
                    showLoading()
                }

            } else {
                showAllowLocationGroup()
                Toast.makeText(requireContext(), "Location permission denied", Toast.LENGTH_SHORT).show()
            }
        }
    }


    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        init()
        setUpRecyclerViews()
        val weatherModel = arguments?.getSerializable("weather") as RootWeatherModel?
        if (weatherModel != null) {
            binding.tvCity.text =weatherModel.timezone

            binding.tvDegree.addTemperature(weatherModel.current?.temp!!, requireContext())
            binding.tvFdegree.addTemperature(weatherModel.current?.feelsLike!!, requireContext())
            binding.tvDescription.text =
                weatherModel.current?.weather?.get(0)?.description ?: ""
            binding.tvDate.text = Convertor.convertDtToDate(requireContext(),weatherModel.current?.dt)
            binding.tvWindSpeed.addWindSpeedInMile(weatherModel.current?.windSpeed!!,requireContext())
            binding.humidity.text = weatherModel.current?.humidity.toString() + "%"
            binding.tvPressure.text = weatherModel.current?.pressure.toString() + "hpa"
            binding.tvVisibility.text = weatherModel.current?.visibility.toString() + "m"
            binding.tvCloud.text = weatherModel.current?.clouds.toString() + "m"
            binding.tvUltra.text = weatherModel.current?.uvi.toString() + "%"
            binding.imvMainicon.setImageResource(
                Convertor.convertIconToDrawableImage(
                    weatherModel.current?.weather?.get(0)?.icon
                )
            )
            dailyRecyclerAdapter.submitList(weatherModel.daily)
            hourlyRecyclerAdapter.submitList(weatherModel.hourly)
        }
        else{
            homeFragmentViewModelFactory = HomeViewModelFactory(
                Repository.getInstance(
                    WeatherApiClient.getInstance(),
                    ConcreteLocalSource.getInstance(WeatherDatabase.getInstance(requireContext()).weatherDao()),
                    LocationManager(view.context),
                    SettingsSharedPreferences.getInstance(view.context),
                )
            )
            viewModel = ViewModelProvider(
                this,
                homeFragmentViewModelFactory
            )[HomeFragmentViewModel::class.java]


            if (!LocationManager.checkLocationPermissions(requireContext())) {

                showAllowLocationGroup()

                // Set click listener for "Enable Location" button
                binding.btnAllowLocation.setOnClickListener {
                    // Request location permission
                    requestPermissions(
                        arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                        LOCATION_PERMISSION_ID)
                }
            }
            else{
                println("Location is allowed >>><")
                lifecycleScope.launchWhenCreated {
                    viewModel.apiState.collectLatest { result ->
                        when (result) {
                            is APIState.Loading -> showLoading()
                            is APIState.Success -> {
                                showHomeGroup()
                                binding.tvCity.text = Constants.getCityNameByLatAndLon(
                                    requireContext(),
                                    result.data.lat,
                                    result.data.lon
                                )

                                binding.tvDegree.addTemperature(result.data.current?.temp!!, requireContext())
                                binding.tvFdegree.addTemperature(result.data.current?.feelsLike!!, requireContext())
                                binding.tvDescription.text =
                                    result.data.current?.weather?.get(0)?.description ?: ""
                                binding.tvDate.text = Convertor.convertDtToDate(requireContext(),result.data.current?.dt)
                                binding.tvWindSpeed.addWindSpeedInMile(result.data.current?.windSpeed!!,requireContext())
                                binding.humidity.text = result.data.current?.humidity.toString() + "%"
                                binding.tvPressure.text = result.data.current?.pressure.toString() + "hpa"
                                binding.tvVisibility.text = result.data.current?.visibility.toString() + "m"
                                binding.tvCloud.text = result.data.current?.clouds.toString() + "m"
                                binding.tvUltra.text = result.data.current?.uvi.toString() + "%"
                                binding.imvMainicon.setImageResource(
                                    Convertor.convertIconToDrawableImage(
                                        result.data.current?.weather?.get(0)?.icon
                                    )
                                )
                                dailyRecyclerAdapter.submitList(result.data.daily)
                                hourlyRecyclerAdapter.submitList(result.data.hourly)

                            }
                            else -> {
                                binding.homeProgressBar.visibility = View.GONE

                            }
                        }
                    }
                }
            }
            /*else{
                lifecycleScope.launchWhenCreated {
                    viewModel.apiState.collectLatest { result ->
                        when (result) {
                            is APIState.Loading -> showLoading()
                            is APIState.Success -> {
                                showHomeGroup()
                                binding.tvCity.text = Constants.getCityNameByLatAndLon(
                                    view.context,
                                    result.data.lat,
                                    result.data.lon
                                )

                                binding.tvDegree.addTemperature(result.data.current?.temp!!, requireContext())
                                binding.tvFdegree.addTemperature(result.data.current?.feelsLike!!, requireContext())
                                binding.tvDescription.text =
                                    result.data.current?.weather?.get(0)?.description ?: ""
                                binding.tvDate.text = Convertor.convertDtToDate(requireContext(),result.data.current?.dt)
                                binding.tvWindSpeed.addWindSpeedInMile(result.data.current?.windSpeed!!,requireContext())
                                binding.humidity.text = result.data.current?.humidity.toString() + "%"
                                binding.tvPressure.text = result.data.current?.pressure.toString() + "hpa"
                                binding.tvVisibility.text = result.data.current?.visibility.toString() + "m"
                                binding.tvCloud.text = result.data.current?.clouds.toString() + "m"
                                binding.tvUltra.text = result.data.current?.uvi.toString() + "%"
                                binding.imvMainicon.setImageResource(
                                    Convertor.convertIconToDrawableImage(
                                        result.data.current?.weather?.get(0)?.icon
                                    )
                                )
                                dailyRecyclerAdapter.submitList(result.data.daily)
                                hourlyRecyclerAdapter.submitList(result.data.hourly)

                            }
                            else -> {
                                binding.homeProgressBar.visibility = View.GONE

                            }
                        }
                    }
                }
            }*/


        }


    }

    private fun init() {
        hourlyRecyclerAdapter = HourlyWeatherAdabter()
        dailyRecyclerAdapter = DailyWeatherAdabter()
    }

    private fun setUpRecyclerViews() {
        binding.rvHourlyWeather.apply {
            adapter = hourlyRecyclerAdapter.apply {
                submitList(ArrayList())
            }
            setHasFixedSize(false)
            layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
        }
        binding.rvDailyWeather.apply {
            adapter = dailyRecyclerAdapter.apply {
                submitList(ArrayList())
            }
            setHasFixedSize(false)
            layoutManager = LinearLayoutManager(context)
        }
    }

    private fun showLoading() {
        binding.homeProgressBar.visibility = View.VISIBLE
        binding.homeGroup.visibility = View.GONE
        binding.allowLocationGroup.visibility=View.GONE
    }
    private fun showHomeGroup(){
        binding.homeProgressBar.visibility = View.GONE
        binding.allowLocationGroup.visibility=View.GONE
        binding.homeGroup.visibility = View.VISIBLE
    }
    private fun showAllowLocationGroup(){
        binding.allowLocationGroup.visibility=View.VISIBLE
        binding.homeGroup.visibility = View.GONE
        binding.homeProgressBar.visibility = View.GONE
    }

}
