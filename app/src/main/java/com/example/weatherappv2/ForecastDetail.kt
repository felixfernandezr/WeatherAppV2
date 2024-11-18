package com.example.weatherappv2

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weatherappv2.adapter.forecastadapter.ForecastAdapter
import com.example.weatherappv2.databinding.ForecastDetailLayoutBinding
import com.example.weatherappv2.model.WeatherModel

class ForecastDetail : AppCompatActivity() {

    private lateinit var binding: ForecastDetailLayoutBinding
    private lateinit var forecastAdapter: ForecastAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize ForecastDetail binding
        binding = ForecastDetailLayoutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Retrieving data from MainActivity
        val weatherData = intent.getSerializableExtra("WEATHER_DATA") as? WeatherModel

        // Initialize ForecastDetail RecyclerView and Adapter
        weatherData?.let {
            forecastAdapter = ForecastAdapter(it.forecast.forecastday)
            binding.rvForecastDetail.layoutManager = LinearLayoutManager(this)
            binding.rvForecastDetail.adapter = forecastAdapter
        }
    }
}