package com.example.weatherandroidapplication.network

import com.example.weatherandroidapplication.models.WeatherClass

class NetworkOps {
    private val city = "Mumbai"
    private val country = "IN"
    private val key = "2facb83973524c8e927e726516722a3d"
    suspend fun getWeatherInfo():ArrayList<WeatherClass>{
        val cityList:List<String> = listOf("Mumbai","Delhi","Kolkata","Chennai")
        var WeatherObjectList = ArrayList<WeatherClass>()
        for (city in cityList){
            WeatherObjectList.add(WeatherApi.retrofitService.getWeatherdata(city!!, country!!, key!!))
        }

        return WeatherObjectList
    }
}
