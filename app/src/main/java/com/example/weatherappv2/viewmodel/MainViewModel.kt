package com.example.weatherappv2.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherappv2.api.Constants
import com.example.weatherappv2.api.RetrofitClient
import com.example.weatherappv2.model.WeatherModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    private val weatherApi = RetrofitClient.weatherApi
    private val _weatherResult = MutableLiveData<WeatherModel>()
    val weatherResult: LiveData<WeatherModel> = _weatherResult

    fun fetchData(city: String){
        viewModelScope.launch(Dispatchers.IO){
            try {
                val response = weatherApi.getWeather(Constants.apiKey, city, "7")
                if (response.isSuccessful) {
                    // Check if the response body is not null and contains forecasts
                    Log.d("payload", response.body().toString())
                    response.body()?.let { forecastModel ->
                        _weatherResult.postValue(forecastModel) // Post the list of WeatherModel
                    } ?: run {
                        Log.e("Error: ", "Response body is null")
                    }
                } else {
                    Log.e("Error : ", "Response Code: ${response.code()} - ${response.message()}")
                }
            } catch (e: Exception) {
                Log.e("Exception : ", e.message ?: "Unknown error")
            }
        }
    }
}