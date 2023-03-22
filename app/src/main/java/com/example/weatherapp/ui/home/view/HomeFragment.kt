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
import com.example.weatherapp.databinding.FragmentHomeBinding
import com.example.weatherapp.helper.Constants
import com.example.weatherapp.helper.Convertor
import com.example.weatherapp.data.repo.Repository
import com.example.weatherapp.data.source.db.ConcreteLocalSource
import com.example.weatherapp.data.source.network.APIState
import com.example.weatherapp.data.source.network.WeatherApiClient
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

        homeFragmentViewModelFactory = HomeViewModelFactory(
            Repository.getInstance(
                WeatherApiClient.getInstance(),
                ConcreteLocalSource(view.context)
            )
        )
        viewModel = ViewModelProvider(
            this,
            homeFragmentViewModelFactory
        ).get(HomeFragmentViewModel::class.java)
        init()
        setUpRecyclerViews()
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
                        println(
                            "The city : " + Constants.getCityNameByLatAndLon(
                                view.context,
                                result.data.lat,
                                result.data.lon
                            )
                        )
                        binding.tvDegree.text = String.format(
                            "%.1f",
                            result.data.current?.temp
                        ) + "${Constants.Celsius}"
                        binding.tvFdegree.text = String.format(
                            "%.1f",
                            result.data.current?.feelsLike
                        ) + "${Constants.Celsius}"
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
