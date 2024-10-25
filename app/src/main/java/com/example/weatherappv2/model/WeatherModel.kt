package com.example.weatherappv2.model

import java.io.Serializable

data class WeatherModel(
    val current: Current,
    val forecast: Forecast,
    val location: Location
) : Serializable