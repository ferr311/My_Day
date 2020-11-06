package com.shukhaev.mydaytest.weather.repository

import com.shukhaev.mydaytest.weather.model.InfoCity
import com.shukhaev.mydaytest.weather.model.Weather
import com.shukhaev.mydaytest.weather.network.WeatherApiClient

class WeatherRepository {

    suspend fun getWeather(lat: Double, lon: Double): Weather? {
        val res = WeatherApiClient.weatherApiClient.getWeather(lat = lat, lon = lon)

        return res.body()?.weather
    }

    suspend fun getCityInfo(lat: Double, lon: Double): InfoCity? {
        val res = WeatherApiClient.weatherApiClient.getWeather(lat = lat, lon = lon)

        return res.body()?.infoCity
    }
}