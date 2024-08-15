package com.mtt.weatherapp.domain.usecases

import androidx.lifecycle.LiveData
import com.mtt.weatherapp.domain.repository.WeatherRepository
import javax.inject.Inject

class GetLocationStatusUseCase @Inject constructor(
    val repository: WeatherRepository
) {
    operator fun invoke(): LiveData<Boolean> {
        return repository.getLocationStatus()
    }
}
