package com.mtt.weatherapp.data.remote.dto

import com.google.gson.annotations.SerializedName

data class WeatherResponse(

	@field:SerializedName("current")
	val current: Current? = null,

	@field:SerializedName("daily")
	val daily: Daily? = null,

	@field:SerializedName("hourly")
	val hourly: Hourly? = null,

	)

data class Hourly(

	@field:SerializedName("temperature_2m")
	val temperature2m: List<Double>? = null,

	@field:SerializedName("time")
	val time: List<String?>? = null,
	@field:SerializedName("weather_code")
	val weatherCode: List<Int>? = null
)

data class Daily(

	@field:SerializedName("time")
	val time: List<String?>? = null,

	@field:SerializedName("weather_code")
	val weatherCode: List<Int?>? = null,
	@field:SerializedName("temperature_2m_max")
	val temperature2mMax: List<Double>? = null,
	@field:SerializedName("temperature_2m_min")
	val temperature2mMin: List<Double>? = null,

	)

data class Current(

	@field:SerializedName("pressure_msl")
	val pressureMsl: Any? = null,

	@field:SerializedName("wind_speed_10m")
	val windSpeed10m: Double? = null,

	@field:SerializedName("temperature_2m")
	val temperature2m: Double? = null,

	@field:SerializedName("relative_humidity_2m")
	val relativeHumidity2m: Int? = null,

	@field:SerializedName("interval")
	val interval: Int? = null,

	@field:SerializedName("weather_code")
	val weatherCode: Int? = null
)
