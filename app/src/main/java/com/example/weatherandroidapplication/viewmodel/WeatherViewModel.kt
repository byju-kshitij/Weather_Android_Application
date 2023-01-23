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
import kotlinx.coroutines.launch
import retrofit2.Response

class WeatherViewModel : ViewModel() {
    var weatherResponse : WeatherClass by mutableStateOf(WeatherClass(0, listOf()))

    var errorMessage : String by mutableStateOf("")
    var city:String? = null
    var country:String? = null
    var key:String? = null
    fun getWeatherData() : WeatherClass{
        var weatherDataResult : WeatherClass = WeatherClass(0, listOf())
        println("Inside getWeatherData")
        viewModelScope.launch{
            val apiService = WeatherApiService.getInstance()
            try{
//                println("From ViewModel try block")
//                val weatherObj = apiService.getWeatherdata(city!!,country!!,key!!)
//                println(" weatherObj is ")
//                println(weatherObj)
//                weatherResponse = weatherObj
                val retrofitData = WeatherApi.retrofitService.getWeatherdata(city!!,country!!,key!!)
                weatherResponse = retrofitData
//                println("Temperature is:")
//                println(retrofitData.data[0].temp)
            }

            catch (e:Exception){
                errorMessage = e.message.toString()
            }
        }

        return weatherDataResult
    }


}