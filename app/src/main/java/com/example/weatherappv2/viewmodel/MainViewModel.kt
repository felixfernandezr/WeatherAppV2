package com.example.weatherappv2.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherappv2.api.ApiResult
import com.example.weatherappv2.api.Constants
import com.example.weatherappv2.api.RetrofitClient
import com.example.weatherappv2.model.WeatherModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    private val weatherApi = RetrofitClient.weatherApi
    private val _weatherResult = MutableLiveData<ApiResult<WeatherModel>>()
    val weatherResult: LiveData<ApiResult<WeatherModel>> = _weatherResult

    fun fetchData(city: String){
        _weatherResult.postValue(ApiResult.Loading) // Indicate loading state

        viewModelScope.launch(Dispatchers.IO){
            try {
                val response = weatherApi.getWeather(Constants.apiKey, city, "7")
                if (response.isSuccessful) {
                    Log.d("payload", response.body().toString())
                    // Check if the response body is not null and contains forecasts
                    response.body()?.let { forecastModel ->
                        _weatherResult.postValue(ApiResult.Success(forecastModel)) // Post the list of WeatherModel
                    } ?: run {
                        _weatherResult.postValue(ApiResult.Error("Response body is null"))
                    }
                } else {
                    _weatherResult.postValue(ApiResult.Error("Error: ${response.code()} - ${response.message()}"))
                }
            } catch (e: Exception) {
                _weatherResult.postValue(ApiResult.Error(e.message ?: "Unknown error"))
            }
        }
    }
}