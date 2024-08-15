package com.mtt.weatherapp.presentation.screens.homeScreen

import android.Manifest
import android.app.Activity
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresExtension
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.mtt.weatherapp.R
import com.mtt.weatherapp.common.Utils
import com.mtt.weatherapp.presentation.components.CenterAlignLargeText
import com.mtt.weatherapp.presentation.components.CenterAlignNormalText
import com.mtt.weatherapp.presentation.components.DailyWeatherComponent
import com.mtt.weatherapp.presentation.components.HourlyWeatherComponent
import com.mtt.weatherapp.presentation.components.WeatherStatsComponent
import com.mtt.weatherapp.presentation.components.animatedGradientBackground
import com.mtt.weatherapp.presentation.theme.primaryLight
import kotlinx.coroutines.launch
import org.json.JSONObject

private const val LOCATION_PERMISSION_REQUEST_CODE = 1001

@RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
@Composable
fun HomeScreenContent(viewModel: HomeScreenViewModel = hiltViewModel(), json: JSONObject) {
    val location by viewModel.locationData.observeAsState()
    val locationName by viewModel.locationName.observeAsState()
    val isLocationEnabled by viewModel.locationStatus.observeAsState(false)
    val context = LocalContext.current
    val permissionGranted by viewModel.permissionGranted.observeAsState(false)

    val coroutineScope = rememberCoroutineScope()
    // Check permissions and location status on launch
    LaunchedEffect(Unit) {
        viewModel.checkPermissions(context)
    }
    // Handle changes in permissions and location status
    LaunchedEffect(permissionGranted) {
        if (permissionGranted) {
            viewModel.checkLocationEnabled()
        } else {
            ActivityCompat.requestPermissions(
                context as Activity,
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                LOCATION_PERMISSION_REQUEST_CODE
            )
        }
    }
    LaunchedEffect(permissionGranted, isLocationEnabled) {
        if (permissionGranted && isLocationEnabled) {
            coroutineScope.launch {
                viewModel.fetchLastLocation()
            }
        } else if (!isLocationEnabled) {
            Toast.makeText(context, "Location is Disabled", Toast.LENGTH_LONG).show()
        }
    }
    LaunchedEffect(location) {
        location?.let {
            viewModel.fetchLocationName(it)

        }
    }

    LaunchedEffect(locationName) {
        locationName?.let {
            viewModel.getWeather(location!!.latitude, location!!.longitude)

        }
    }

    val state = viewModel.state.value
    state.weather?.let { weatherResponse ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(animatedGradientBackground())
                .padding(top = 32.dp)
        ) {
            locationName?.let {
                CenterAlignNormalText(
                    text = it, textColor = primaryLight
                )
            }
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                CenterAlignLargeText(text = buildString {
                    append(weatherResponse.current?.temperature2m?.toInt())
                    append("\u00B0")
                    append("C")
                }, textColor = Color(0xFF2B1204))
                AsyncImage(
                    model =
                    Utils.getImageUrl(
                        json,
                        weatherResponse.current?.weatherCode.toString()
                    ),
                    modifier = Modifier
                        .width(50.dp)
                        .height(50.dp),
                    contentScale = ContentScale.Inside,
                    contentDescription = "Weather Icon"
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp), horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                WeatherStatsComponent(
                    statsName = "Wind",
                    statsValue = "${weatherResponse.current?.windSpeed10m} km/h",
                    image = R.drawable.wind
                )
                WeatherStatsComponent(
                    statsName = "Pressure",
                    statsValue = "758 mm Hg",
                    image = R.drawable.gauge
                )
                WeatherStatsComponent(
                    statsName = "Humidity",
                    statsValue = "28%",
                    image = R.drawable.humidity
                )
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp, bottom = 16.dp, start = 24.dp, end = 24.dp)
                    .border(2.dp, color = Color.White, shape = RoundedCornerShape(25.dp))
            ) {
                LazyRow(modifier = Modifier.padding(start = 10.dp, end = 10.dp)) {
                    items(viewModel.getHourlyData()) {
                        HourlyWeatherComponent(hourly = it, json = json)
                    }
                }
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp, bottom = 16.dp, start = 24.dp, end = 24.dp)
                    .border(2.dp, color = Color.White, shape = RoundedCornerShape(25.dp))
            ) {
                LazyColumn(
                    modifier = Modifier.padding(
                        start = 10.dp,
                        end = 10.dp,
                        top = 10.dp,
                        bottom = 10.dp
                    )
                )
                {
                    items(viewModel.getDailyData()) {
                        DailyWeatherComponent(dailyDto = it, json = json)
                    }
                }
            }

        }
    }
}

@Preview
@Composable
fun Preview() {
    Row(
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        CenterAlignLargeText(text = buildString {
            append(35)
            append("\u00B0")
            append("C")
        }, textColor = Color(0xFF2B1204))
        Icon(
            imageVector = Icons.Filled.Delete,
            modifier = Modifier
                .width(50.dp)
                .height(50.dp),
            contentDescription = "Weather Icon"
        )
    }
}