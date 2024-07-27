package com.mtt.weatherapp.data.remote.dto

data class DailyDto(
    val date: String,
    val weatherCode: Int,
    val minTemp: Double,
    val maxTemp: Double
)