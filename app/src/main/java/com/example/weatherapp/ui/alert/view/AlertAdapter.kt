package com.example.weatherapp.ui.alert.view

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.R
import com.example.weatherapp.data.model.Alerts
import com.example.weatherapp.databinding.AddAlertItemBinding
import com.example.weatherapp.data.model.RootWeatherModel
import com.example.weatherapp.helper.AlertType
import com.example.weatherapp.helper.Convertor

class AlertAdapter(var deleteAlert: (Alerts) -> Unit) :
    ListAdapter<Alerts, AlertAdapter.ViewHolder>(DailyDiffUtil()) {

    class ViewHolder(private val binding: AddAlertItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val tvStartTime: TextView = binding.tvStartAlertTimeItem
        val tvEndTime: TextView = binding.tvEndAlertTimeItem
        val tvEndDate: TextView = binding.tvAlertEndDate
        val tvStartDate: TextView = binding.tvAlertStartDate
        val imgDelete: ImageView = binding.imgvDeleteAletItem
        val imgIcon: ImageView = binding.imvAlertType
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            AddAlertItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        println("The start is: ${getItem(position).start} The end is : ${getItem(position).end}")
        holder.tvStartTime.text = Convertor.convertDtToTime(holder.tvStartTime.context,getItem(position).start)
        holder.tvEndTime.text = Convertor.convertDtToTime(holder.tvEndTime.context,getItem(position).end)
        holder.tvStartDate.text = Convertor.convertDtToNumberDate(holder.tvStartDate.context,getItem(position).start)
        holder.tvEndDate.text = Convertor.convertDtToNumberDate(holder.tvEndDate.context,getItem(position).end)
        if (getItem(position).type == AlertType.NOTIFICATION.name) {
            holder.imgIcon.setImageResource(R.drawable.alarm_notif)
        } else {
            holder.imgIcon.setImageResource(R.drawable.aler)
        }

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