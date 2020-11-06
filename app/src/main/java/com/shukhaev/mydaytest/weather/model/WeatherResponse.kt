package com.shukhaev.mydaytest.weather.model

import com.google.gson.annotations.SerializedName

data class WeatherResponse(
    @SerializedName("fact")
    val weather: Weather,
    @SerializedName("info")
    val infoCity: InfoCity
)