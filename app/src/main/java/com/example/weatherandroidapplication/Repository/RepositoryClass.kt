package com.example.weatherandroidapplication.Repository

import androidx.lifecycle.MutableLiveData
import com.example.weatherandroidapplication.WeatherModel
import com.example.weatherandroidapplication.models.DAO
import com.example.weatherandroidapplication.models.WeatherClass
import com.example.weatherandroidapplication.network.NetworkOps

class RepositoryClass {

    val networkObj = NetworkOps()

    val daoObj = DAO()

    suspend fun getFromNetwokCall() : ArrayList<WeatherClass>{

        return networkObj.getWeatherInfo()

    }

    fun getFromDB(): MutableLiveData<List<WeatherModel>> {
        return daoObj.getAllWeather()
    }

    fun adddToDB(WeatherObjectList:ArrayList<WeatherClass>){

        for(item in WeatherObjectList){
            daoObj.addWeatherToDB(item.data[0].temp,item.data[0].weather.description,item.data[0].city_name)
                //daoObj.addWeatherToDB(temp,weatherDescription,city)
        }

    }

    fun deleteAllDBData(){
        daoObj.deleteAllWeatherData()
    }

}