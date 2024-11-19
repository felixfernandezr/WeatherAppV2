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
import com.example.weatherappv2.database.AppDatabase
import com.example.weatherappv2.database.SearchHistoryDAO
import com.example.weatherappv2.databinding.ActivityMainBinding
import com.example.weatherappv2.model.WeatherModel
import com.example.weatherappv2.viewmodel.MainViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels()
    private lateinit var searchHistoryDao: SearchHistoryDAO
    private lateinit var appDatabase: AppDatabase
    private lateinit var currentWeatherAdapter: CurrentWeatherAdapter
    private lateinit var weatherData: WeatherModel
    private val searchHistory = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize Room database
        appDatabase = AppDatabase.getDatabase(this)
        searchHistoryDao = appDatabase.searchHistoryDao()

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

                if (!searchHistory.contains(weatherData.location.name)) {
                    searchHistory.add(weatherData.location.name) // Add only if it's not already in the list
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


        // Navigate to history fragment through button
        binding.showSearchHistoryBtn.setOnClickListener {
            val fragment = HistoryFragment()

            // Passing search list to the fragment
            val bundle = Bundle()
            bundle.putStringArrayList("searchList", ArrayList(searchHistory))
            fragment.arguments = bundle

            // Hide MainActivity layout
            binding.main.visibility = View.GONE

            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, fragment) // Replace container with the fragment
                .addToBackStack(null)
                .commit()
        }
    }

    // Adding funcionality to OS back button
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