package com.mtt.weatherapp.domain.repository

import com.mtt.weatherapp.data.remote.dto.WeatherResponse

interface WeatherRepository {
    suspend fun getWeather(map: Map<String, String>): WeatherResponse
}