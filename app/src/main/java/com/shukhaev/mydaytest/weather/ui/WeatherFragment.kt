package com.shukhaev.mydaytest.weather.ui

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.core.app.ActivityCompat
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import com.github.twocoffeesoneteam.glidetovectoryou.GlideToVectorYou
import com.google.android.gms.location.LocationServices
import com.shukhaev.mydaytest.R
import com.shukhaev.mydaytest.util.APP_ACTIVITY
import com.shukhaev.mydaytest.util.toast
import kotlinx.android.synthetic.main.fragment_weather.*

class WeatherFragment : Fragment(R.layout.fragment_weather) {

    private val weatherViewModel: WeatherViewModel by viewModels()
//    private val cityInfoViewModel: WeatherViewModel by viewModels()

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        checkPermission()
        observeWeatherState()
        weather_fab_refresh.setOnClickListener { getNewLocation() }

    }

    private fun observeWeatherState() {
//        cityInfoViewModel.infoCity.observe(viewLifecycleOwner) {
//            val cityUrl: String? = it.url
//            weather_city.text = cityUrl?.toUri()?.lastPathSegment ?: ""
////            if (cityUrl==null){
////                weather_city.text = ""
////            }else{
////                val city = cityUrl
////                weather_city.text = city
////            }
//        }

        weatherViewModel.weather.observe(viewLifecycleOwner) {
            val temp: String? = it.temp.toString()
            val tempFeels: String? = it.feels_like.toString()
            val windSpeed: String? = it.wind_speed.toString()
            val pressure: String? = it.pressure.toString()
            val humidity: String? = it.humidity.toString()
            val windDir: String? = it.wind_dir.toString()
            if (temp == null) {
                weather_current_temperature.text = "null"
            } else {
                weather_current_temperature.text = temp//it.temp.toString()
            }
            if (tempFeels == null) {
                weather_feel_temperature.text = "null"
            } else {
                weather_feel_temperature.text = tempFeels//it.feels_like.toString()
            }
            if (windSpeed == null) {
                weather_wind.text = "null"
            } else {
                weather_wind.text = "$windSpeed m/c"//it.wind_speed.toString()
            }
            if (pressure == null) {
                weather_pressure.text = "null"
            } else {
                weather_pressure.text = "$pressure mm"//it.pressure.toString()
            }
            when (windDir) {
                "nw" -> weather_wind_direction.text = "СЗ"
                "n" -> weather_wind_direction.text = "С"
                "ne" -> weather_wind_direction.text = "СВ"
                "e" -> weather_wind_direction.text = "В"
                "se" -> weather_wind_direction.text = "ЮВ"
                "s" -> weather_wind_direction.text = "Ю"
                "sw" -> weather_wind_direction.text = "ЮЗ"
                "w" -> weather_wind_direction.text = "З"
                "c" -> weather_wind_direction.text = "штиль"
                else -> weather_wind_direction.text = ""
            }

            val icon = it.icon
            val iconUrl = "https://yastatic.net/weather/i/icons/blueye/color/svg/$icon.svg"
            GlideToVectorYou.justLoadImage(APP_ACTIVITY, iconUrl.toUri(), weather_icon)
        }
    }

    private fun checkPermission() {
        val isLocationPermGranted = ActivityCompat.checkSelfPermission(
            APP_ACTIVITY,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        if (isLocationPermGranted) {
            getNewLocation()
        } else {
            getPermission()
        }
    }

    private fun getPermission() {
        requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), PERMISSION_CODE)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_CODE && grantResults.all { it == PackageManager.PERMISSION_GRANTED }) {
            getNewLocation()
        } else {
            toast("We cant get weather without location")
        }
    }

    private fun getWeather(lat: Double, lon: Double) {
        weatherViewModel.getWeather(lat, lon)
//        cityInfoViewModel.getCityInfo(lat, lon)
    }

    @SuppressLint("MissingPermission")
    private fun getNewLocation() {
        LocationServices.getFusedLocationProviderClient(requireContext()).lastLocation
            .addOnSuccessListener {
                it?.let {
                    val lat = it.latitude
                    val lon = it.longitude
                    getWeather(lat, lon)

                } ?: toast("Service location not avalieble")
            }
            .addOnFailureListener { toast("Location dont get") }
            .addOnCanceledListener { toast("Request was canceled") }
    }

    companion object {
        private const val PERMISSION_CODE = 3648
    }

}