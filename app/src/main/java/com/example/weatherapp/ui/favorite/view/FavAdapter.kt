package com.example.weatherapp.ui.favorite.view

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.databinding.AddFavItemBinding
import com.example.weatherapp.helper.Constants
import com.example.weatherapp.helper.Convertor
import com.example.weatherapp.data.model.RootWeatherModel

class FavAdapter(var deleteItem: (RootWeatherModel) -> Unit,
                 var viewItem: (RootWeatherModel) -> Unit) :
    ListAdapter<RootWeatherModel, FavAdapter.ViewHolder>( DailyDiffUtil()) {

    class ViewHolder(private val binding: AddFavItemBinding) : RecyclerView.ViewHolder(binding.root) {
        val city: TextView = binding.tvCityNameFavItem
        val delete: ImageView = binding.imgvDeleteFavItem
        val cvViewItem: CardView = binding.cvFavItem
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = AddFavItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
       // holder.city.text = Constants.getCityNameByLatAndLon(holder.city.context,getItem(position).lat,getItem(position).lon)
        holder.city.text = getItem(position).timezone
        //println("The city name is : ${Constants.getCityNameByLatAndLon(holder.city.context,getItem(position).lat,getItem(position).lon)}")
        holder.delete.setOnClickListener {
            deleteItem(getItem(position))
        }
        holder.cvViewItem.setOnClickListener {
            viewItem(getItem(position))
        }

    }
}

class DailyDiffUtil : DiffUtil.ItemCallback<RootWeatherModel>() {
    override fun areItemsTheSame(oldItem: RootWeatherModel, newItem: RootWeatherModel): Boolean {
        return oldItem === newItem
    }

    override fun areContentsTheSame(oldItem: RootWeatherModel, newItem: RootWeatherModel): Boolean {
        return oldItem == newItem
    }

}