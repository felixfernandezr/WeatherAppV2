package com.example.weatherappv2.model

import java.io.Serializable

data class Forecast(
    val forecastday: List<Forecastday>
) : Serializable