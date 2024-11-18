package com.example.weatherappv2.adapter.forecastadapter

import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherappv2.databinding.ItemForecastDetailBinding
import com.example.weatherappv2.model.Forecastday
import com.example.weatherappv2.model.WeatherModel

class ForecastViewHolder (private val binding: ItemForecastDetailBinding) : RecyclerView.ViewHolder(binding.root) {

    fun render(forecast: Forecastday) {
        Log.d("DataFromRender", "Data from render function")

        binding.tvDate.text = forecast.date
        binding.tvCondition.text = forecast.day.condition.text
        binding.tvMin.text = "${forecast.day.mintemp_c}°C"
        binding.tvMax.text = "${forecast.day.maxtemp_c}°C"
    }
}