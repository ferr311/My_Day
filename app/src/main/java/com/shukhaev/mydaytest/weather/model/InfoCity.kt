package com.shukhaev.mydaytest.weather.model

import com.google.gson.annotations.SerializedName

data class InfoCity(
    @SerializedName("url")
    val url: String?
)