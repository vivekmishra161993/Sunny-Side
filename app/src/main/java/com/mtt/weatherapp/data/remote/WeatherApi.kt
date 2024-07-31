package com.mtt.weatherapp.data.remote

import com.mtt.weatherapp.data.remote.dto.WeatherResponse
import retrofit2.http.GET
import retrofit2.http.QueryMap

interface WeatherApi {
    @GET("forecast")
    suspend fun getWeather(@QueryMap(encoded = true) parameters: Map<String, String>): WeatherResponse

}