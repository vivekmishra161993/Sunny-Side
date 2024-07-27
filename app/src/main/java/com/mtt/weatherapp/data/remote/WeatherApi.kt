package com.mtt.weatherapp.data.remote

import com.mtt.weatherapp.data.remote.dto.WeatherResponse
import retrofit2.http.GET

interface WeatherApi {
    @GET("forecast?latitude=28.6654&longitude=77.4391&current=temperature_2m,relative_humidity_2m,weather_code,pressure_msl,wind_speed_10m&hourly=temperature_2m,weather_code&daily=weather_code,temperature_2m_max,temperature_2m_min&forecast_hours=12&timezone=Asia%2FTokyo")
    suspend fun getWeather(): WeatherResponse

}