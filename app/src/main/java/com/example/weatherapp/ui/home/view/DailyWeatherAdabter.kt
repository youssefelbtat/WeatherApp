package com.example.weatherapp.ui.home.view

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.helper.Constants
import com.example.weatherapp.databinding.DaysItemBinding
import com.example.weatherapp.helper.Convertor
import com.example.weatherapp.data.model.Daily


class DailyWeatherAdabter() :
    ListAdapter<Daily, DailyWeatherAdabter.ViewHolder>( DailyDiffUtil()) {
    
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
        holder.day.text = Convertor.convertDtToDay(getItem(position).dt)
        holder.des.text = getItem(position).weather[0].description
        holder.icon.setImageResource(Convertor.convertIconToDrawableImage(getItem(position).weather[0].icon))
        holder.unit.text = Constants.Celsius
        holder.temp.text ="${getItem(position).temp?.max} / ${getItem(position).temp?.min}"

    }
}

class DailyDiffUtil : DiffUtil.ItemCallback<Daily>() {
    override fun areItemsTheSame(oldItem: Daily, newItem: Daily): Boolean {
        return oldItem === newItem
    }

    override fun areContentsTheSame(oldItem: Daily, newItem: Daily): Boolean {
        return oldItem == newItem
    }

}