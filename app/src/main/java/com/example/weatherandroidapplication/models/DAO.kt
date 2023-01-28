package com.example.weatherandroidapplication.models

import androidx.lifecycle.MutableLiveData
import com.example.weatherandroidapplication.WeatherModel
import io.realm.Realm
import java.util.*

class DAO {
    var isDataBaseEmpty:Boolean = true

    private var realm: Realm = Realm.getDefaultInstance()

    fun deleteAllWeatherData() {
        realm.executeTransaction { r: Realm ->
            r.delete(WeatherModel::class.java)
        }
    }

    fun getAllWeather(): MutableLiveData<List<WeatherModel>> {
        val list = MutableLiveData<List<WeatherModel>>()
        val weathers = realm.where(WeatherModel::class.java).findAll()
        list.value = weathers?.subList(0, weathers.size)
        return list
    }
    fun addWeatherToDB(temp: Int, weatherDescription: String,city:String) {
        isDataBaseEmpty = false
        realm.executeTransaction { r: Realm ->
            val weather = r.createObject(WeatherModel::class.java, UUID.randomUUID().toString())
            weather.temp = temp!!
            weather.city = city!!
            weather.description = weatherDescription!!
            realm.insertOrUpdate(weather)
        }
    }

}