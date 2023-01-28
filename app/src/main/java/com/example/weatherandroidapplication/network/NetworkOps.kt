package com.example.weatherandroidapplication.network

class NetworkOps {
    private val city = "Mumbai"
    private val country = "IN"
    private val key = "2facb83973524c8e927e726516722a3d"
    suspend fun getWeatherInfo(){
        val retrofitData = WeatherApi.retrofitService.getWeatherdata(city!!, country!!, key!!)
    }
}