package com.shukhaev.mydaytest.weather.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shukhaev.mydaytest.weather.repository.WeatherRepository
import com.shukhaev.mydaytest.weather.model.InfoCity
import com.shukhaev.mydaytest.weather.model.Weather
import kotlinx.coroutines.launch

class WeatherViewModel : ViewModel() {

    private val repo = WeatherRepository()

    private val weatherLiveData: MutableLiveData<Weather> = MutableLiveData()
    val weather: LiveData<Weather>
        get() = weatherLiveData

    private val cityInfoLiveData: MutableLiveData<InfoCity> = MutableLiveData()
    val infoCity: LiveData<InfoCity>
        get() = cityInfoLiveData

    fun getWeather(lat: Double, lon: Double) {
        viewModelScope.launch {
            val res = repo.getWeather(lat, lon)
            weatherLiveData.postValue(res)
        }
    }

    fun getCityInfo(lat: Double, lon: Double) {
        viewModelScope.launch {
            val res = repo.getCityInfo(lat, lon)
            cityInfoLiveData.postValue(res)
        }
    }
}