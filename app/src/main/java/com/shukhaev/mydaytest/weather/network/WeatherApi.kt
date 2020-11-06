package com.shukhaev.mydaytest.weather.network

import com.shukhaev.mydaytest.weather.model.WeatherResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface WeatherApi {

    @GET("v1/informers")
    suspend fun getWeather(
        @Header("X-Yandex-API-Key") token: String = "8df85a2d-de57-4e99-be0f-4d7cb50a67ef",
        @Query("lat") lat: Double,
        @Query("lon") lon: Double
    ): Response<WeatherResponse>

}