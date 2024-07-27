package com.mtt.weatherapp.data.remote.dto

data class HourlyDto(
    val time: String? = null,
    val temp: String? = null,
    val weatherCode: Int? = null
)