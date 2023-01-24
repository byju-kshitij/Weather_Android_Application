package com.example.weatherandroidapplication.viewmodel

import WeatherX
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherandroidapplication.Models.WeatherClass
import com.example.weatherandroidapplication.network.WeatherApi
import com.example.weatherandroidapplication.network.WeatherApiService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.Response

class WeatherViewModel : ViewModel() {
    val weatherResponse = MutableStateFlow(WeatherClass(0, listOf()))
    val stateFlow = weatherResponse.asStateFlow()

    var errorMessage : String by mutableStateOf("")
    var city:String? = null
    var country:String? = null
    var key:String? = null
    var weatherDataResult : WeatherClass = WeatherClass(0, listOf())
    fun getWeatherData() : WeatherClass{

        println("Inside getWeatherData")
        viewModelScope.launch{
            val apiService = WeatherApiService.getInstance()
            try{

                val retrofitData = WeatherApi.retrofitService.getWeatherdata(city!!,country!!,key!!)
                weatherResponse.value = retrofitData
                println("Temperature from view Model is:")
                //println(weatherDataResult.data[0].temp)
            }

            catch (e:Exception){
                errorMessage = e.message.toString()
            }
        }

        println("From View Model layer, the weather object is : ")
        println(weatherDataResult.data)
        return weatherDataResult
    }


}