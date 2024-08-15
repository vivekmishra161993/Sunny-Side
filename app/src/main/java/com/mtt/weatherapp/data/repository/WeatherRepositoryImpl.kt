package com.mtt.weatherapp.data.repository

import android.content.Context
import android.content.IntentFilter
import android.location.LocationManager
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.mtt.weatherapp.data.LocationReceiver
import com.mtt.weatherapp.data.remote.WeatherApi
import com.mtt.weatherapp.data.remote.dto.WeatherResponse
import com.mtt.weatherapp.domain.repository.WeatherRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WeatherRepositoryImpl @Inject constructor(
    private val api: WeatherApi,
    private val context: Context
) : WeatherRepository {

    private val _locationStatus = MutableLiveData<Boolean>()
    private val locationBroadcastReceiver = LocationReceiver { isEnabled ->
        _locationStatus.postValue(isEnabled)
    }

    init {
        val filter = IntentFilter(LocationManager.PROVIDERS_CHANGED_ACTION)
        context.registerReceiver(locationBroadcastReceiver, filter)
        checkLocationEnabled()
    }

    override fun checkLocationEnabled() {
        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val isEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
        _locationStatus.value = isEnabled
    }

    override fun getLocationStatus(): LiveData<Boolean> {
        return _locationStatus
    }

    fun unregisterReceiver() {
        context.unregisterReceiver(locationBroadcastReceiver)
    }
    override suspend fun getWeather(map: Map<String, String>): WeatherResponse {
        return api.getWeather(map)
    }


}