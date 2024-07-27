package com.mtt.weatherapp.presentation.screens.homeScreen

import com.mtt.weatherapp.data.remote.dto.WeatherResponse

data class HomeScreenState(
    val isLoading: Boolean = false,
    val weather: WeatherResponse? = null,
    val error: String = ""
)
