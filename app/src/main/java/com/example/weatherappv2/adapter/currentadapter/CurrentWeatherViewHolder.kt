package com.example.weatherappv2.adapter.currentadapter

import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherappv2.databinding.ItemActivityMainBinding
import com.example.weatherappv2.model.WeatherModel

class CurrentWeatherViewHolder(private val binding: ItemActivityMainBinding) : RecyclerView.ViewHolder(binding.root) {

    fun render(currentWeather: WeatherModel) {
        Log.d("FromViewHolderData", "Data from view holder: $currentWeather")

        binding.tvLocation.text = "${currentWeather.location.name}, ${currentWeather.location.country}"
        binding.tvTemp.text = "${currentWeather.current.temp_c}°C"
        binding.tvCondition.text = currentWeather.current.condition.text
        binding.tvHumidity.text = "Humidity: ${currentWeather.current.humidity}%"
        binding.tvWind.text = "Wind: ${currentWeather.current.wind_kph} km/h"
    }
}