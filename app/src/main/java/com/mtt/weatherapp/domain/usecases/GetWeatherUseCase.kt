package com.mtt.weatherapp.domain.usecases

import android.net.http.HttpException
import android.os.Build
import androidx.annotation.RequiresExtension
import com.mtt.weatherapp.common.Resource
import com.mtt.weatherapp.data.remote.dto.WeatherResponse
import com.mtt.weatherapp.domain.repository.WeatherRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.IOException
import javax.inject.Inject

class GetWeatherUseCase @Inject constructor(private val repository: WeatherRepository) {
    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    operator fun invoke(lat: Double, lon: Double): Flow<Resource<WeatherResponse>> = flow {
        try {
            emit(Resource.Loading())
            val map = HashMap<String, String>()
            map["latitude"] = lat.toString()
            map["longitude"] = lon.toString()
            map["current"] =
                "temperature_2m,relative_humidity_2m,weather_code,pressure_msl,wind_speed_10m"
            map["hourly"] = "temperature_2m,weather_code"
            map["daily"] = "weather_code,temperature_2m_max,temperature_2m_min"
            map["forecast_hours"] = "12"
            map["timezone"] = "Asia%2FTokyo"
            map["timeformat"] = "unixtime"
            val weatherResponse = repository.getWeather(map)
            emit(Resource.Success(weatherResponse))
        } catch (e: HttpException) {
            emit(Resource.Error(e.localizedMessage ?: "An unexpected error occurred"))
        } catch (e: IOException) {
            emit(Resource.Error("Couldn't connect to Internet"))
        }
    }
}