package com.example.weatherapp.ui.home.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import com.example.weatherapp.data.source.network.APIState
import com.example.weatherapp.data.source.network.WeatherApiClient
import com.example.weatherapp.helper.addTemperature
import com.example.weatherapp.ui.home.viewmodel.HomeFragmentViewModel
import com.example.weatherapp.ui.home.viewmodel.HomeViewModelFactory
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


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        setUpRecyclerViews()
        val weatherModel = arguments?.getSerializable("weather") as RootWeatherModel?
        if (weatherModel != null) {
            binding.tvCity.text = Constants.getCityNameByLatAndLon(
                view.context,
                weatherModel.lat,
                weatherModel.lon
            )

            binding.tvDegree.addTemperature(weatherModel.current?.temp!!, requireContext())
            binding.tvFdegree.addTemperature(weatherModel.current?.feelsLike!!, requireContext())
            binding.tvDescription.text =
                weatherModel.current?.weather?.get(0)?.description ?: ""
            binding.tvDate.text = Convertor.convertDtToDate(weatherModel.current?.dt)
            binding.tvWindSpeed.text = weatherModel.current?.windSpeed.toString() + "m/s"
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
                    ConcreteLocalSource(view.context),
                    LocationManager(view.context),
                    SettingsSharedPreferences.getInstance(view.context),
                )
            )
            viewModel = ViewModelProvider(
                this,
                homeFragmentViewModelFactory
            ).get(HomeFragmentViewModel::class.java)

            lifecycleScope.launchWhenCreated {
                viewModel.apiState.collectLatest { result ->
                    when (result) {
                        is APIState.Loading -> showLoading()
                        is APIState.Success -> {
                            binding.homeProgressBar.visibility = View.GONE
                            binding.homeGroup.visibility = View.VISIBLE
                            binding.tvCity.text = Constants.getCityNameByLatAndLon(
                                view.context,
                                result.data.lat,
                                result.data.lon
                            )

                            binding.tvDegree.addTemperature(result.data.current?.temp!!, requireContext())
                            binding.tvFdegree.addTemperature(result.data.current?.feelsLike!!, requireContext())
                            binding.tvDescription.text =
                                result.data.current?.weather?.get(0)?.description ?: ""
                            binding.tvDate.text = Convertor.convertDtToDate(result.data.current?.dt)
                            binding.tvWindSpeed.text = result.data.current?.windSpeed.toString() + "m/s"
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
    }

}
