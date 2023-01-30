package com.example.weatherandroidapplication.viewmodel

import WeatherX
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherandroidapplication.Repository.RepositoryClass
import com.example.weatherandroidapplication.WeatherModel
import com.example.weatherandroidapplication.models.Data
import com.example.weatherandroidapplication.models.WeatherClass
import com.example.weatherandroidapplication.network.WeatherApi
import com.example.weatherandroidapplication.network.WeatherApiService
import com.example.weatherandroidapplication.viewmodel.WeatherRequest.Ideal
import io.realm.Realm
import io.realm.Realm.getApplicationContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.*
import kotlin.collections.ArrayList

sealed class WeatherRequest {
    object Ideal : WeatherRequest()
    object Loading : WeatherRequest()
    data class Success(val data: ArrayList<WeatherClass>) : WeatherRequest() // val data : List<WeatherClass> for 4 cities, use Lazy Column for list
    data class Error(val error: String) : WeatherRequest()
}

class WeatherViewModel : ViewModel() {
    private var weatherResponse = MutableStateFlow<WeatherRequest>(Ideal)
    val stateFlow = weatherResponse.asStateFlow()

    var isDataBaseEmpty:Boolean = true

    private var realm: Realm = Realm.getDefaultInstance()

    private var RepoObj = RepositoryClass()

    //    var errorMessage : String by mutableStateOf("")
    var city: String? = null
    var country: String? = null
    var key: String? = null
    var weatherDataResult: WeatherClass = WeatherClass(0, arrayListOf() )
    fun getWeatherData() {
        println("Inside getWeatherData")
        weatherResponse.value = WeatherRequest.Loading
        viewModelScope.launch {
            val apiService = WeatherApiService.getInstance()
            try {
                var WeatherObjectList = ArrayList<WeatherClass>()
                WeatherObjectList = RepoObj.getFromNetwokCall()
                //TODO : Add in the database

                //delay(2000)
                weatherResponse.value = WeatherRequest.Success(WeatherObjectList)

                RepoObj.adddToDB(WeatherObjectList)
                //
                println("Temperature from view Model is:")
                //println(weatherDataResult.data[0].temp)
            } catch (e: Exception) {
                // if api fails
                //TODO : Get from database
                var history : MutableLiveData<List<WeatherModel>>
                history = RepoObj.getFromDB()


                print("Data from Database ")

                if(history.value?.isEmpty()==true){
                    weatherResponse.value = WeatherRequest.Error(e.message.toString())
                }
                else{
                    Toast.makeText(getApplicationContext(),"No Internet !! Fetched From DB",Toast.LENGTH_SHORT).show();
                    var sizeOfWeatherList:Int = history.value!!.count()

                    var WeatherObjectList = ArrayList<WeatherClass>()
                    for(i in sizeOfWeatherList-4..sizeOfWeatherList-1){
                        var lastUpdatedWeatherModelObject : WeatherModel = history.value!![i]

                        var latestTemp:Int = lastUpdatedWeatherModelObject.temp
                        var latestDescription:String = lastUpdatedWeatherModelObject.description!!
                        var latestCity:String = lastUpdatedWeatherModelObject.city!!
                        var latestWeatherObject : WeatherClass = WeatherClass(1, arrayListOf())
                        latestWeatherObject.data.add(Data())
                        latestWeatherObject.data[0].temp = latestTemp
                        latestWeatherObject.data[0].weather.description = latestDescription
                        latestWeatherObject.data[0].city_name = latestCity

                        //Sample Initialisations
                        latestWeatherObject.data[0].sunrise = lastUpdatedWeatherModelObject.sunrise!!
                        latestWeatherObject.data[0].sunset = lastUpdatedWeatherModelObject.sunset!!
                        latestWeatherObject.data[0].rh = lastUpdatedWeatherModelObject.humidity!!
                        latestWeatherObject.data[0].vis = lastUpdatedWeatherModelObject.visibility!!
                        latestWeatherObject.data[0].clouds = lastUpdatedWeatherModelObject.clouds!!
                        latestWeatherObject.data[0].wind_spd = lastUpdatedWeatherModelObject.winds!!
                        latestWeatherObject.data[0].pres = lastUpdatedWeatherModelObject.pressure!!
                        latestWeatherObject.data[0].ob_time = lastUpdatedWeatherModelObject.ob_time!!
                        latestWeatherObject.data[0].weather.code = lastUpdatedWeatherModelObject.icon!!


                        WeatherObjectList.add(latestWeatherObject)

                    }

                    weatherResponse.value = WeatherRequest.Success(WeatherObjectList)
//                    var lastUpdatedWeatherModelObject : WeatherModel = history.value!![history.value!!.count()-1]
//                    var latestTemp:Int = lastUpdatedWeatherModelObject.temp
//                    var latestDescription:String = lastUpdatedWeatherModelObject.description!!
//                    var latestWeatherObject : WeatherClass = WeatherClass(1, arrayListOf())
//                    latestWeatherObject.data.add(Data())
//                    latestWeatherObject.data[0].temp = latestTemp
//                    latestWeatherObject.data[0].weather.description = latestDescription
//                    weatherResponse.value = WeatherRequest.Success(latestWeatherObject)
//                    for (item in history.value!!){
//                        println(item)
//                    }
                }


                //weatherResponse.value = WeatherRequest.Error(e.message.toString())

            }
        }

        println("From View Model layer, the weather object is : ")
        println(weatherDataResult.data)
    }

//    fun deleteAllWeatherData() {
//        realm.executeTransaction { r: Realm ->
//            r.delete(WeatherModel::class.java)
//        }
//    }
//
//    fun getAllWeather(): MutableLiveData<List<WeatherModel>> {
//        val list = MutableLiveData<List<WeatherModel>>()
//        val weathers = realm.where(WeatherModel::class.java).findAll()
//        list.value = weathers?.subList(0, weathers.size)
//        return list
//    }
//    fun addWeatherToDB(temp: Int, weatherDescription: String,city:String) {
//        isDataBaseEmpty = false
//        realm.executeTransaction { r: Realm ->
//            val weather = r.createObject(WeatherModel::class.java, UUID.randomUUID().toString())
//            weather.temp = temp!!
//            weather.city = city!!
//            weather.description = weatherDescription!!
//            realm.insertOrUpdate(weather)
//        }
//    }

}