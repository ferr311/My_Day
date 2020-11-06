package com.shukhaev.mydaytest.weather.network

import androidx.core.net.toUri
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object WeatherApiClient {

    const val BASE_URL = "https://api.weather.yandex.ru/"

    val weatherApiClient: WeatherApi by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        return@lazy retrofit.create(WeatherApi::class.java)
    }

}