package com.mtt.weatherapp.presentation.screens.homeScreen

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresExtension
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mtt.weatherapp.common.Resource
import com.mtt.weatherapp.data.remote.dto.DailyDto
import com.mtt.weatherapp.data.remote.dto.HourlyDto
import com.mtt.weatherapp.domain.usecases.GetWeatherUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject


@RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    private val getWeatherUseCase: GetWeatherUseCase
) : ViewModel() {
    private val _state = mutableStateOf(HomeScreenState())
    val state: State<HomeScreenState> = _state

    init {
        getWeather()
    }

    private fun getWeather() {
        getWeatherUseCase().onEach { result ->
            when (result) {
                is Resource.Success -> {
                    _state.value = HomeScreenState(weather = result.data)
                }

                is Resource.Error -> {
                    _state.value =
                        HomeScreenState(error = result.message ?: "An unexpected error occurred")
                }

                is Resource.Loading -> {
                    _state.value = HomeScreenState(isLoading = true)
                }
            }
        }.launchIn(viewModelScope)
    }

    fun getHourlyData(): ArrayList<HourlyDto> {
        val hourlyList = ArrayList<HourlyDto>()
        val timeData = state.value.weather?.hourly?.time
        val weatherCode = state.value.weather?.hourly?.weatherCode
        state.value.weather?.hourly?.temperature2m?.forEachIndexed { index, temp ->
            val dateTime = LocalDateTime.parse(timeData?.get(index))
            // Define the desired output format (AM/PM)
            val formatter = DateTimeFormatter.ofPattern("hh:mm a")
            // Format the time part of the LocalDateTime object
            val formattedTime = dateTime.format(formatter)
            hourlyList.add(HourlyDto(formattedTime, "$temp\u00B0 C", weatherCode?.get(index)))
        }
        return hourlyList
    }

    @SuppressLint("SimpleDateFormat")
    fun getDailyData(): ArrayList<DailyDto> {
        val dailyList = ArrayList<DailyDto>()
        val timeData = state.value.weather?.daily?.time
        val weatherCode = state.value.weather?.daily?.weatherCode
        val minTemp = state.value.weather?.daily?.temperature2mMin
        val maxTemp = state.value.weather?.daily?.temperature2mMax
        timeData?.forEachIndexed { index, value ->
            val inFormat = SimpleDateFormat("yyyy-MM-dd")
            val date = value?.let { inFormat.parse(it) }
            val outFormat = SimpleDateFormat("EEEE")
            val dayName = date?.let { outFormat.format(it) }
            dailyList.add(
                DailyDto(
                    dayName!!,
                    weatherCode?.get(index)!!,
                    minTemp?.get(index)!!,
                    maxTemp?.get(index)!!
                )
            )
        }

        return dailyList
    }
}