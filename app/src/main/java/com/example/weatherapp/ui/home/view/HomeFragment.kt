package com.example.weatherapp.ui.home.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.R
import com.example.weatherapp.model.getDamyList

class HomeFragment : Fragment() {

    private lateinit var hourlyRecyclerView: RecyclerView
    private lateinit var hourlyRecyclerAdapter: HourlyWeatherAdabter
    private lateinit var dailyRecyclerView: RecyclerView
    private lateinit var dailyRecyclerAdapter: DailyWeatherAdabter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init(view)
        hourlyRecyclerView.apply {
            adapter=hourlyRecyclerAdapter.apply {
                submitList(getDamyList.mutableList)
            }
            setHasFixedSize(false)
            layoutManager= LinearLayoutManager(context).apply {
                orientation=RecyclerView.HORIZONTAL
            }
        }
        dailyRecyclerView.apply {
            adapter=dailyRecyclerAdapter.apply {
                submitList(getDamyList.dailyWeatherList)
            }
            setHasFixedSize(false)
            layoutManager= LinearLayoutManager(context).apply {
                orientation=RecyclerView.VERTICAL
            }
        }
    }
    fun init(view:View){
        hourlyRecyclerView=view.findViewById(R.id.rv_hourly_weather)
        hourlyRecyclerAdapter = HourlyWeatherAdabter()
        dailyRecyclerView=view.findViewById(R.id.rv_daily_weather)
        dailyRecyclerAdapter= DailyWeatherAdabter()
    }
}