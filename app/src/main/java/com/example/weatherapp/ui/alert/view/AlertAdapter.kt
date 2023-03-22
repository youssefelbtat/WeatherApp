package com.example.weatherapp.ui.alert.view

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.data.model.Alerts
import com.example.weatherapp.databinding.AddAlertItemBinding
import com.example.weatherapp.data.model.RootWeatherModel

class AlertAdapter(var deleteAlert: (Alerts) -> Unit) :
    ListAdapter<Alerts, AlertAdapter.ViewHolder>( DailyDiffUtil()) {

    class ViewHolder(private val binding: AddAlertItemBinding) : RecyclerView.ViewHolder(binding.root) {
        val tvStartTime: TextView = binding.tvStartAlertItem
        val tvEndTime: TextView = binding.tvEndAlertItem
        val imgDelete: ImageView = binding.imgvDeleteAletItem
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = AddAlertItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tvStartTime.text=getItem(position).start.toString()
        holder.tvEndTime.text=getItem(position).end.toString()
        holder.imgDelete.setOnClickListener {
            deleteAlert(getItem(position))
        }

    }
}

class DailyDiffUtil : DiffUtil.ItemCallback<Alerts>() {
    override fun areItemsTheSame(oldItem: Alerts, newItem: Alerts): Boolean {
        return oldItem === newItem
    }

    override fun areContentsTheSame(oldItem: Alerts, newItem: Alerts): Boolean {
        return oldItem == newItem
    }

}