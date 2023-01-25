package com.example.weatherandroidapplication.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherandroidapplication.models.Data
import com.example.weatherandroidapplication.models.WeatherClass
import com.example.weatherandroidapplication.network.WeatherApi
import com.example.weatherandroidapplication.network.WeatherApiService
import com.example.weatherandroidapplication.viewmodel.WeatherRequest.Ideal
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class WeatherRequest {
    object Ideal : WeatherRequest()
    object Loading : WeatherRequest()
    data class Success(val data: WeatherClass) : WeatherRequest()
    data class Error(val error: String) : WeatherRequest()
}

class WeatherViewModel : ViewModel() {
    private var weatherResponse = MutableStateFlow<WeatherRequest>(Ideal)
    val stateFlow = weatherResponse.asStateFlow()

    //    var errorMessage : String by mutableStateOf("")
    var city: String? = null
    var country: String? = null
    var key: String? = null
    var weatherDataResult: WeatherClass = WeatherClass(0, listOf())
    fun getWeatherData() {
        println("Inside getWeatherData")
        weatherResponse.value = WeatherRequest.Loading
        viewModelScope.launch {
            val apiService = WeatherApiService.getInstance()
            try {
                val retrofitData = WeatherApi.retrofitService.getWeatherdata(city!!, country!!, key!!)
                delay(2000)
                weatherResponse.value = WeatherRequest.Success(retrofitData)
                //
                println("Temperature from view Model is:")
                //println(weatherDataResult.data[0].temp)
            } catch (e: Exception) {
                // if api fails
                weatherResponse.value = WeatherRequest.Error(e.message.toString())
            }
        }

        println("From View Model layer, the weather object is : ")
        println(weatherDataResult.data)
    }

}