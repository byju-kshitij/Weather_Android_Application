package com.example.weatherandroidapplication.viewmodel

import WeatherX
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherandroidapplication.WeatherModel
import com.example.weatherandroidapplication.models.Data
import com.example.weatherandroidapplication.models.WeatherClass
import com.example.weatherandroidapplication.network.WeatherApi
import com.example.weatherandroidapplication.network.WeatherApiService
import com.example.weatherandroidapplication.viewmodel.WeatherRequest.Ideal
import io.realm.Realm
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.*

sealed class WeatherRequest {
    object Ideal : WeatherRequest()
    object Loading : WeatherRequest()
    data class Success(val data: WeatherClass) : WeatherRequest() // val data : List<WeatherClass> for 4 cities, use Lazy Column for list
    data class Error(val error: String) : WeatherRequest()
}

class WeatherViewModel : ViewModel() {
    private var weatherResponse = MutableStateFlow<WeatherRequest>(Ideal)
    val stateFlow = weatherResponse.asStateFlow()

    private var realm: Realm = Realm.getDefaultInstance()

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
                //TODO : Add in the database

                //delay(2000)
                weatherResponse.value = WeatherRequest.Success(retrofitData)

                addWeatherToDB(retrofitData.data[0].temp,retrofitData.data[0].weather.description,city!!)
                //
                println("Temperature from view Model is:")
                //println(weatherDataResult.data[0].temp)
            } catch (e: Exception) {
                // if api fails
                //TODO : Get from database
                var history : MutableLiveData<List<WeatherModel>>
                history = getAllWeather()
                print("Data from Database")
                println(history)
                weatherResponse.value = WeatherRequest.Error(e.message.toString())
            }
        }

        println("From View Model layer, the weather object is : ")
        println(weatherDataResult.data)
    }

    private fun getAllWeather(): MutableLiveData<List<WeatherModel>> {
        val list = MutableLiveData<List<WeatherModel>>()
        val weathers = realm.where(WeatherModel::class.java).findAll()
        list.value = weathers?.subList(0, weathers.size)
        return list
    }
    fun addWeatherToDB(temp: Int, weatherDescription: String,city:String) {
        realm.executeTransaction { r: Realm ->
            val weather = r.createObject(WeatherModel::class.java, UUID.randomUUID().toString())
            weather.temp = temp!!
            weather.city = city!!
            weather.description = weatherDescription!!
            realm.insertOrUpdate(weather)
        }
    }

}