package com.example.weatherappv2.model

import java.io.Serializable

data class Condition(
    val code: String,
    val icon: String,
    val text: String
) : Serializable