package com.example.weatherappv2.api

import com.example.weatherappv2.model.WeatherModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("forecast.json")
    suspend fun getWeather(
        @Query("key") apiKey: String,
        @Query("q") city:String,
        @Query("days") days:String
    ): Response<WeatherModel>
}