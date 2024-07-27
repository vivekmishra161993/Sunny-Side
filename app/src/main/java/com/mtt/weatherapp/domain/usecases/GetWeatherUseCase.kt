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
    operator fun invoke(): Flow<Resource<WeatherResponse>> = flow {
        try {
            emit(Resource.Loading())
            val weatherResponse = repository.getWeather()
            emit(Resource.Success(weatherResponse))
        } catch (e: HttpException) {
            emit(Resource.Error(e.localizedMessage ?: "An unexpected error occurred"))
        } catch (e: IOException) {
            emit(Resource.Error("Couldn't connect to Internet"))
        }
    }
}