package com.mtt.weatherapp.presentation.screens.homeScreen

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.os.Build
import androidx.annotation.RequiresExtension
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.location.FusedLocationProviderClient
import com.mtt.weatherapp.common.Resource
import com.mtt.weatherapp.common.Utils.awaitLastLocation
import com.mtt.weatherapp.data.remote.dto.DailyDto
import com.mtt.weatherapp.data.remote.dto.HourlyDto
import com.mtt.weatherapp.data.repository.WeatherRepositoryImpl
import com.mtt.weatherapp.domain.usecases.GetLocationStatusUseCase
import com.mtt.weatherapp.domain.usecases.GetWeatherUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.withContext
import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale
import javax.inject.Inject


@RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    @ApplicationContext private val appContext: Context,
    private val getWeatherUseCase: GetWeatherUseCase,
    private val getLocationStatusUseCase: GetLocationStatusUseCase,
    private val fusedLocationProviderClient: FusedLocationProviderClient
) : ViewModel() {
    private val _state = mutableStateOf(HomeScreenState())
    val state: State<HomeScreenState> = _state
    private val _locationData = MutableLiveData<Location?>()
    val locationData: LiveData<Location?> get() = _locationData
    private val _locationName = MutableLiveData<String?>()
    val locationName: LiveData<String?> get() = _locationName
    val locationStatus: LiveData<Boolean> = getLocationStatusUseCase()
    private val _permissionGranted = MutableLiveData<Boolean>()
    val permissionGranted: LiveData<Boolean> get() = _permissionGranted
    fun getWeather(lat: Double, lon: Double) {
        getWeatherUseCase(lat, lon).onEach { result ->
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

            // Format the time part of the LocalDateTime object
            val formattedTime =
                convertUnixTimestampToLocalTime(timeData?.get(index)!!.toLong(), "hh:mm a")
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
            val dayName = convertUnixTimestampToLocalTime(value!!.toLong(), "EEEE")

            dailyList.add(
                DailyDto(
                    dayName,
                    weatherCode?.get(index)!!,
                    minTemp?.get(index)!!,
                    maxTemp?.get(index)!!
                )
            )
        }

        return dailyList
    }

    suspend fun fetchLastLocation() {
        withContext(Dispatchers.IO) {
            val location = fusedLocationProviderClient.awaitLastLocation()
            _locationData.postValue(location)

        }
    }

    suspend fun fetchLocationName(location: Location) {
        withContext(Dispatchers.IO) {
            try {
                val geocoder = Geocoder(appContext, Locale.getDefault())
                val addresses = geocoder.getFromLocation(location.latitude, location.longitude, 1)
                val address = addresses?.firstOrNull()
                _locationName.postValue(address?.locality)
            } catch (e: Exception) {
                e.printStackTrace()
                _locationName.postValue("Unknown location")
            }
        }
    }

    private fun convertUnixTimestampToLocalTime(timestamp: Long, pattern: String): String {
        val instant = Instant.ofEpochSecond(timestamp)
        val zoneId = ZoneId.systemDefault()
        val zonedDateTime = ZonedDateTime.ofInstant(instant, zoneId)
        val formatter = DateTimeFormatter.ofPattern(pattern)
        return zonedDateTime.format(formatter)
    }

    fun checkPermissions(context: Context): Boolean {
        val fineLocationPermission = ContextCompat.checkSelfPermission(
            context, Manifest.permission.ACCESS_FINE_LOCATION
        )
        val coarseLocationPermission = ContextCompat.checkSelfPermission(
            context, Manifest.permission.ACCESS_COARSE_LOCATION
        )
        return fineLocationPermission == PackageManager.PERMISSION_GRANTED &&
                coarseLocationPermission == PackageManager.PERMISSION_GRANTED
    }

    fun checkLocationEnabled() {
        (getLocationStatusUseCase.repository as WeatherRepositoryImpl).checkLocationEnabled()
    }

    fun updatePermissionsGranted(isGranted: Boolean) {
        _permissionGranted.value = isGranted
    }

    override fun onCleared() {
        super.onCleared()
        (getLocationStatusUseCase.repository as WeatherRepositoryImpl).unregisterReceiver()

    }
}
