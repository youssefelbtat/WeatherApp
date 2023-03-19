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
import com.example.weatherapp.model.HourlyWeatherModel

class HourlyWeatherAdabter(

) :
    ListAdapter<HourlyWeatherModel, HourlyWeatherAdabter.ViewHolder>( HourlyDiffUtil()) {

    class ViewHolder(private val itemView: View) : RecyclerView.ViewHolder(itemView) {
        val time: TextView
            get() = itemView.findViewById(R.id.tv_time)
        val degree: TextView
            get() = itemView.findViewById(R.id.tv_degree_in_rv_item)
        val icon : ImageView
            get() =itemView.findViewById(R.id.imv_hourly_icon_in_rv)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.hourly_items, parent, false))
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