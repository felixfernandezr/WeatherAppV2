package com.example.weatherappv2.model

import java.io.Serializable

data class Forecastday(
    val astro: Astro,
    val date: String,
    val date_epoch: String,
    val day: Day,
    val hour: List<Hour>
) : Serializable