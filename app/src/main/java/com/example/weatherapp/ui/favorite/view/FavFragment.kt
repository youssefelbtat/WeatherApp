package com.example.weatherapp.ui.favorite.view

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weatherapp.data.location.LocationManager
import com.example.weatherapp.data.repo.Repository
import com.example.weatherapp.data.source.db.ConcreteLocalSource
import com.example.weatherapp.data.source.network.APIState
import com.example.weatherapp.data.source.network.WeatherApiClient
import com.example.weatherapp.databinding.FragmentFavBinding
import com.example.weatherapp.helper.Constants
import com.example.weatherapp.helper.Constants.EXTRA_LATITUDE
import com.example.weatherapp.helper.Constants.EXTRA_LONGITUDE
import com.example.weatherapp.helper.Constants.REQUEST_CODE_MAPS_ACTIVITY_TO_FAV
import com.example.weatherapp.helper.Constants.showDialog
import com.example.weatherapp.helper.Constants.showToast
import com.example.weatherapp.ui.favorite.viewmodel.FavViewModel
import com.example.weatherapp.ui.favorite.viewmodel.FavViewModelFactory
import com.example.weatherapp.ui.map.MapsActivity

import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class FavFragment : Fragment() {
    private lateinit var binding: FragmentFavBinding
    private lateinit var favFragmentViewModelFactory: FavViewModelFactory
    private lateinit var viewModel: FavViewModel
    private lateinit var favRecyclerAdapter: FavAdapter

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_MAPS_ACTIVITY_TO_FAV && resultCode == Activity.RESULT_OK) {
            val latitude = data?.getDoubleExtra(EXTRA_LATITUDE, 0.0)
            val longitude = data?.getDoubleExtra(EXTRA_LONGITUDE, 0.0)
            if (latitude != null && longitude != null) {
                lifecycleScope.launch {
                    viewModel.getWeather(latitude, longitude).collect { rootWeatherModel ->
                        viewModel.addFavWeather(rootWeatherModel)
                    }
                }
            }
        }
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
            val intent = Intent(activity, MapsActivity::class.java)
            startActivityForResult(intent, REQUEST_CODE_MAPS_ACTIVITY_TO_FAV)

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
            deleteItem = {  item->
                showDialog(view.context) {
                    viewModel.removeFavWeather(item)
                }
            },
            viewItem = { weatherModel ->
                showToast(view.context,weatherModel.timezoneOffset.toString())
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