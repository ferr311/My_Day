package com.shukhaev.mydaytest.weather.model

import com.google.gson.annotations.SerializedName

data class Weather(
    val temp: Int?,
    val feels_like: Int?,
    val icon: String?,
    val wind_speed: Double?,
    val wind_dir: String?,
    @SerializedName("pressure_mm")
    val pressure: Int?,
    val humidity: Int?
)