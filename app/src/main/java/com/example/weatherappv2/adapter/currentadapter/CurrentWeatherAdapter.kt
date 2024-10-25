package com.example.weatherappv2.adapter.currentadapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherappv2.databinding.ItemActivityMainBinding
import com.example.weatherappv2.model.WeatherModel

class CurrentWeatherAdapter(
    private var payload: List<WeatherModel>
) : RecyclerView.Adapter<CurrentWeatherViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CurrentWeatherViewHolder {
        val binding = ItemActivityMainBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        Log.d("OnCreateViewHolder", "View holder created")
        return CurrentWeatherViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CurrentWeatherViewHolder, position: Int) {
        val item = payload[position]
        holder.render(item)
    }

    override fun getItemCount(): Int = payload.size

    fun updateData(newData: List<WeatherModel>) {
        payload = newData
        Log.d("FromAdapterData", "Data from adapter: $payload")
        notifyDataSetChanged()
    }
}