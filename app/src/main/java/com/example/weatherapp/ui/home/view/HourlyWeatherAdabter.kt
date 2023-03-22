package com.example.weatherapp.ui.home.view

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.databinding.HourlyItemsBinding
import com.example.weatherapp.helper.Constants
import com.example.weatherapp.helper.Convertor
import com.example.weatherapp.data.model.Hourly
import com.example.weatherapp.data.model.HourlyWeatherModel

class HourlyWeatherAdabter(

) :
    ListAdapter<Hourly, HourlyWeatherAdabter.ViewHolder>( HourlyDiffUtil()) {

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
        holder.time.text = Convertor.convertDtToTime(getItem(position).dt)
        holder.degree.text = "${getItem(position).temp?.toInt()}${Constants.Celsius}"
        holder.icon.setImageResource(Convertor.convertIconToDrawableImage(getItem(position).weather[0].icon))

    }
}

class HourlyDiffUtil : DiffUtil.ItemCallback<Hourly>() {
    override fun areItemsTheSame(oldItem: Hourly, newItem: Hourly): Boolean {
        return oldItem === newItem
    }

    override fun areContentsTheSame(oldItem: Hourly, newItem: Hourly): Boolean {
        return oldItem == newItem
    }

}