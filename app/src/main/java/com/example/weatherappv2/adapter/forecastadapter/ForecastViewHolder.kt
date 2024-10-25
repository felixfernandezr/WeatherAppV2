package com.example.weatherappv2.adapter.forecastadapter

import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherappv2.databinding.ItemForecastDetailBinding
import com.example.weatherappv2.model.WeatherModel

class ForecastViewHolder (private val binding: ItemForecastDetailBinding) : RecyclerView.ViewHolder(binding.root) {

    fun render(currentWeather: WeatherModel) {
        Log.d("DataFromRender", "Data from render function")

        binding.tvDate.text = currentWeather.forecast.forecastday[0].date
        binding.tvCondition.text = currentWeather.forecast.forecastday[0].day.condition.text
        binding.tvMin.text = "${currentWeather.forecast.forecastday[0].day.mintemp_c}°C"
        binding.tvMax.text = "${currentWeather.forecast.forecastday[0].day.maxtemp_c}°C"
    }
}