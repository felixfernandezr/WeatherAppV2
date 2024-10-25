package com.example.weatherappv2

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.SearchView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weatherappv2.adapter.currentadapter.CurrentWeatherAdapter
import com.example.weatherappv2.databinding.ActivityMainBinding
import com.example.weatherappv2.model.WeatherModel
import com.example.weatherappv2.viewmodel.MainViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var currentWeatherAdapter: CurrentWeatherAdapter
    private val viewModel: MainViewModel by viewModels()

    private lateinit var weatherData: WeatherModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize MainActivity binding
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize MainActivity RecyclerView and Adapter
        currentWeatherAdapter = CurrentWeatherAdapter(listOf())
        binding.rvMain.layoutManager = LinearLayoutManager(this)
        binding.rvMain.adapter = currentWeatherAdapter


        // Observe weather results from ViewModel
        viewModel.weatherResult.observe(this) { data ->
            Log.d("ObservedData", "Received data: $data")
            data?.let {
                currentWeatherAdapter.updateData(listOf(data)) // Update adapter data
                weatherData = data
                Log.d("SentToAdapterData", "Data sent to adapter: ${listOf(weatherData)}")
            }
        }

        // SearchView listener for user input
        binding.svCity.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (!query.isNullOrEmpty()) {
                    Log.i("query", "Search query: $query")
                    //binding.tvLoading.text = "Loading..."
                    //binding.tvLoading.visibility = View.VISIBLE
                    viewModel.fetchData(query) // Fetch data from ViewModel
                    binding.showForecastBtn.visibility = View.VISIBLE
                    hideKeyboard() // Hide the keyboard after submission
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                binding.showForecastBtn.visibility = View.GONE
                return true // No action needed for text changes
            }
        })

        // Button listener for showing forecast
        binding.showForecastBtn.setOnClickListener {
            // Start ForecastDetail activity with city name
            val intent = Intent(this, ForecastDetail::class.java).apply {
                putExtra("WEATHER_DATA", weatherData)
            }
            startActivity(intent)
        }
    }

    // Hide keyboard function
    private fun hideKeyboard() {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        val view = currentFocus ?: View(this)
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }
}