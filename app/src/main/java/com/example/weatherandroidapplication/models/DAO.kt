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
    fun addWeatherToDB(weatherOb:WeatherClass) {
        isDataBaseEmpty = false
        realm.executeTransaction { r: Realm ->
            val weather = r.createObject(WeatherModel::class.java, UUID.randomUUID().toString())
            weather.temp = weatherOb.data[0].temp!!
            weather.city = weatherOb.data[0].city_name!!
            weather.description = weatherOb.data[0].weather.description!!
            weather.sunrise = weatherOb.data[0].sunrise
            weather.sunset = weatherOb.data[0].sunset
            weather.humidity = weatherOb.data[0].rh
            weather.visibility = weatherOb.data[0].vis
            weather.clouds = weatherOb.data[0].clouds
            weather.winds = weatherOb.data[0].wind_spd
            weather.pressure = weatherOb.data[0].pres
            realm.insertOrUpdate(weather)
        }
    }

}