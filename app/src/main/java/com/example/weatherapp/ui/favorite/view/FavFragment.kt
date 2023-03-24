package com.example.weatherapp.ui.favorite.view

import android.opengl.Visibility
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.R
import com.example.weatherapp.data.location.LocationManager
import com.example.weatherapp.data.model.RootWeatherModel
import com.example.weatherapp.data.repo.Repository
import com.example.weatherapp.data.source.db.ConcreteLocalSource
import com.example.weatherapp.data.source.network.APIState
import com.example.weatherapp.data.source.network.WeatherApiClient
import com.example.weatherapp.databinding.FragmentFavBinding
import com.example.weatherapp.databinding.FragmentHomeBinding
import com.example.weatherapp.helper.Constants
import com.example.weatherapp.helper.Convertor
import com.example.weatherapp.ui.favorite.viewmodel.FavViewModel
import com.example.weatherapp.ui.favorite.viewmodel.FavViewModelFactory
import com.example.weatherapp.ui.home.view.DailyWeatherAdabter
import com.example.weatherapp.ui.home.view.HourlyWeatherAdabter
import com.example.weatherapp.ui.home.viewmodel.HomeFragmentViewModel
import com.example.weatherapp.ui.home.viewmodel.HomeViewModelFactory
import kotlinx.coroutines.flow.collectLatest

class FavFragment : Fragment() {
    private lateinit var binding: FragmentFavBinding
    private lateinit var favFragmentViewModelFactory: FavViewModelFactory
    private lateinit var viewModel: FavViewModel
    private lateinit var favRecyclerAdapter: FavAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        favFragmentViewModelFactory = FavViewModelFactory(
            Repository.getInstance(
                WeatherApiClient.getInstance(),
                ConcreteLocalSource(view.context),
                LocationManager(view.context)
            )
        )
        viewModel = ViewModelProvider(
            this,
            favFragmentViewModelFactory
        ).get(FavViewModel::class.java)

        setUpRecyclerViews(view)

        binding.fabAddFav.setOnClickListener {
            viewModel.addFavWeather(RootWeatherModel(
                lat = 31.5555379,
                lon = 31.075331
            ))
        }

        lifecycleScope.launchWhenCreated {
            viewModel.apiState.collectLatest { result ->
                when (result) {
                    is APIState.Empty -> showEmpty()
                    is APIState.Success -> {
                        showList()
                        favRecyclerAdapter.submitList(result.data)
                    }
                    else -> {

                    }
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFavBinding.inflate(inflater, container, false)
        return binding.root
    }
    private fun setUpRecyclerViews(view: View) {
        favRecyclerAdapter = FavAdapter(
            deleteItem = {  it->
                viewModel.removeFavWeather(it)
            },
            viewItem = { weatherModel ->
                Toast.makeText(view.context, "Hello ${weatherModel.lat}", Toast.LENGTH_SHORT).show()
            }
        )

        binding.rvFav.apply {
            adapter = favRecyclerAdapter.apply {
                submitList(ArrayList())
            }
            setHasFixedSize(false)
            layoutManager = LinearLayoutManager(context)
        }
    }

    private fun showEmpty(){
        println("On Empty:")
        binding.emptyFavGroup.visibility=View.VISIBLE
        binding.rvFav.visibility = View.GONE
    }
    private fun showList(){
        println("On Success:")
        binding.emptyFavGroup.visibility=View.GONE
        binding.rvFav.visibility = View.VISIBLE
    }



}