package com.mtt.weatherapp.data.repository

import com.mtt.weatherapp.data.remote.WeatherApi
import com.mtt.weatherapp.data.remote.dto.WeatherResponse
import com.mtt.weatherapp.domain.repository.WeatherRepository
import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor(private val api: WeatherApi) : WeatherRepository {
    override suspend fun getWeather(): WeatherResponse {
        return api.getWeather()
    }

}