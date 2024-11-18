package com.example.weatherappv2.adapter.forecastadapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherappv2.databinding.ItemForecastDetailBinding
import com.example.weatherappv2.model.Forecastday
import com.example.weatherappv2.model.WeatherModel

class ForecastAdapter(
    private var data: List<Forecastday>
) : RecyclerView.Adapter<ForecastViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ForecastViewHolder {
        val binding = ItemForecastDetailBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        Log.d("OnCreateViewHolder", "View holder created")
        return ForecastViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ForecastViewHolder, position: Int) {
        val item = data[position]
        holder.render(item)
    }

    override fun getItemCount(): Int = data.size

}