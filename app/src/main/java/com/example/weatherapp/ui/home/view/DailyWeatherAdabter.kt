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
import com.example.weatherapp.model.DailyWeatherModel
import com.example.weatherapp.model.HourlyWeatherModel


class DailyWeatherAdabter() :
    ListAdapter<DailyWeatherModel, DailyWeatherAdabter.ViewHolder>( DailyDiffUtil()) {

    class ViewHolder(private val itemView: View) : RecyclerView.ViewHolder(itemView) {
        val day: TextView
            get() = itemView.findViewById(R.id.daily_item_day)
        val unit: TextView
            get() = itemView.findViewById(R.id.daily_item_unit)
        val icon : ImageView
            get() =itemView.findViewById(R.id.img_daily_item_icon)
        val temp:TextView
            get() = itemView.findViewById(R.id.tv_daily_item_temp)
        val des :TextView
            get() = itemView.findViewById(R.id.tv_daily_item_description)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.days_item, parent, false))
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