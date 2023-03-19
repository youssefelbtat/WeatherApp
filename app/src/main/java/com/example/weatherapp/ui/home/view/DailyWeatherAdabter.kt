package com.example.weatherapp.ui.home.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.R
import com.example.weatherapp.databinding.DaysItemBinding
import com.example.weatherapp.model.DailyWeatherModel
import com.example.weatherapp.model.HourlyWeatherModel


class DailyWeatherAdabter() :
    ListAdapter<DailyWeatherModel, DailyWeatherAdabter.ViewHolder>( DailyDiffUtil()) {
    
    class ViewHolder(private val binding: DaysItemBinding) : RecyclerView.ViewHolder(binding.root) {
        val day: TextView = binding.dailyItemDay
        val unit: TextView = binding.dailyItemUnit
        val icon: ImageView = binding.imgDailyItemIcon
        val temp: TextView = binding.tvDailyItemTemp
        val des: TextView = binding.tvDailyItemDescription
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = DaysItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.day.text = getItem(position).day
        holder.des.text = getItem(position).description
        holder.icon.setImageResource(getItem(position).icon)
        holder.unit.text =getItem(position).unit
        holder.temp.text =getItem(position).temp

    }
}

class DailyDiffUtil : DiffUtil.ItemCallback<DailyWeatherModel>() {
    override fun areItemsTheSame(oldItem: DailyWeatherModel, newItem: DailyWeatherModel): Boolean {
        return oldItem === newItem
    }

    override fun areContentsTheSame(oldItem: DailyWeatherModel, newItem: DailyWeatherModel): Boolean {
        return oldItem == newItem
    }

}