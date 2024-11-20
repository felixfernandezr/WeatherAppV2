package com.example.weatherappv2

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.SearchView
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weatherappv2.adapter.currentadapter.CurrentWeatherAdapter
import com.example.weatherappv2.api.ApiResult
import com.example.weatherappv2.database.AppDataBase
import com.example.weatherappv2.database.SearchHistoryDAO
import com.example.weatherappv2.database.models.SearchHistoryItem
import com.example.weatherappv2.databinding.ActivityMainBinding
import com.example.weatherappv2.model.WeatherModel
import com.example.weatherappv2.viewmodel.MainViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels()
    private lateinit var searchHistoryDao: SearchHistoryDAO
    private lateinit var appDatabase: AppDataBase
    private lateinit var currentWeatherAdapter: CurrentWeatherAdapter
    private lateinit var weatherData: WeatherModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize Room database
        appDatabase = AppDataBase.getDatabase(this)
        searchHistoryDao = appDatabase.searchHistoryDao()

        // Initialize MainActivity binding
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize MainActivity RecyclerView and Adapter
        currentWeatherAdapter = CurrentWeatherAdapter(listOf())
        binding.rvMain.layoutManager = LinearLayoutManager(this)
        binding.rvMain.adapter = currentWeatherAdapter

        // Observe weather results from ViewModel
        viewModel.weatherResult.observe(this) { result ->
            when (result) {
                is ApiResult.Loading -> {
                    binding.tvLoading.visibility = View.VISIBLE // Showing loading text
                    binding.rvMain.visibility = View.GONE
                    Log.d("LoadingState", "Loading weather data...")
                }
                is ApiResult.Success -> {
                    binding.tvLoading.visibility = View.GONE // Hiding loading text
                    binding.rvMain.visibility = View.VISIBLE // and showing data

                    val data = result.data
                    currentWeatherAdapter.updateData(listOf(data)) // Update adapter data
                    weatherData = data

                    saveToDataBase(data) // Saving city and country to DB
                }
                is ApiResult.Error -> {
                    binding.tvLoading.visibility = View.GONE
                    binding.rvMain.visibility = View.VISIBLE

                    // Show AlertDialog when data is null
                    AlertDialog.Builder(this)
                        .setTitle("Error")
                        .setMessage("No data received from the server. Please try again.")
                        .setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
                        .show()
                }
            }
        }

        // SearchView listener for user input
        binding.svCity.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (!query.isNullOrEmpty()) {
                    Log.i("query", "Search query: $query")
                    viewModel.fetchData(query) // Fetch data from ViewModel

                    binding.showForecastBtn.visibility = View.VISIBLE
                    binding.showSearchHistoryBtn.visibility = View.VISIBLE
                    hideKeyboard() // Hide the keyboard after submission
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return true // No action needed for text changes
            }
        })


        // Button listener for showing forecast
        binding.showForecastBtn.setOnClickListener {
            // Start ForecastDetail activity with weather data response
            val intent = Intent(this, ForecastDetail::class.java).apply {
                putExtra("WEATHER_DATA", weatherData)
            }
            startActivity(intent)
        }


        // Navigate to history fragment through button
        binding.showSearchHistoryBtn.setOnClickListener {
            val fragment = HistoryFragment()

            // Hide MainActivity layout
            binding.main.visibility = View.GONE

            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, fragment) // Replace container with the fragment
                .addToBackStack(null)
                .commit()
        }
    }


    // Save to Room DataBase
    private fun saveToDataBase(data: WeatherModel) {
        // Extract city name and country from the weather data
        val cityName = data.location.name
        val cityCountry = data.location.country

        // Check if the query (city) has been saved before in Room, if not, save it
        CoroutineScope(Dispatchers.IO).launch {
            runBlocking { // Prevents duplicates by waiting for coroutine to finish
                // Check if the query (city) has been saved before in Room
                val existingSearch = searchHistoryDao.getAllSearchHistory().find {
                    it.cityName == cityName && it.cityCountry == cityCountry
                }

                // If the search history doesn't already contain this city, save it
                if (existingSearch == null) {
                    val searchHistoryItem = SearchHistoryItem(cityName = cityName, cityCountry = cityCountry)
                    searchHistoryDao.insert(searchHistoryItem) // Insert into Room
                    Log.d("SearchHistory", "Search item saved to Room: $cityName, $cityCountry")
                } else {
                    Log.d("SearchHistory", "Search item already exists: $cityName, $cityCountry")
                }
            }
        }
    }


    // Adding functionality to OS back button
    override fun onBackPressed() {
        // Check if there are fragments in the back stack
        if (supportFragmentManager.backStackEntryCount > 0) {
            supportFragmentManager.popBackStack() // Remove the last fragment
            showMainLayout() // Making MainActivity UI visible
        } else {
            super.onBackPressed() // Exit the activity
        }

        // Show the main layout when there are no fragments in the container
        if (supportFragmentManager.backStackEntryCount == 0) {
            showMainLayout()
        }
    }
    // Safely calling MainActivity binding property from fragment
    fun showMainLayout() {
        binding.main.visibility = View.VISIBLE
    }

    // Hide keyboard function
    private fun hideKeyboard() {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        val view = currentFocus ?: View(this)
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }
}