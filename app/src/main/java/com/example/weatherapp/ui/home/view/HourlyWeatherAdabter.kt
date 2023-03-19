package com.example.weatherapp.ui.home.view

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.R
import com.example.weatherapp.databinding.HourlyItemsBinding
import com.example.weatherapp.model.HourlyWeatherModel

class HourlyWeatherAdabter(

) :
    ListAdapter<HourlyWeatherModel, HourlyWeatherAdabter.ViewHolder>( HourlyDiffUtil()) {

    class ViewHolder( binding: HourlyItemsBinding) : RecyclerView.ViewHolder(binding.root) {
        val time: TextView = binding.tvTime
        val degree: TextView = binding.tvDegreeInRvItem
        val icon: ImageView = binding.imvHourlyIconInRv
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val binding = HourlyItemsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.time.text = getItem(position).time
        holder.degree.text = getItem(position).degree
        holder.icon.setImageResource(getItem(position).icon)

    }
}

class HourlyDiffUtil : DiffUtil.ItemCallback<HourlyWeatherModel>() {
    override fun areItemsTheSame(oldItem: HourlyWeatherModel, newItem: HourlyWeatherModel): Boolean {
        return oldItem === newItem
    }

    override fun areContentsTheSame(oldItem: HourlyWeatherModel, newItem: HourlyWeatherModel): Boolean {
        return oldItem == newItem
    }

}