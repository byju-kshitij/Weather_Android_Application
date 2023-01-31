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
    data class Success(val data: ArrayList<WeatherClass>) : WeatherRequest()
    data class Error(val error: String) : WeatherRequest()
}

class WeatherViewModel : ViewModel() {
    private var weatherResponse = MutableStateFlow<WeatherRequest>(Ideal)
    val stateFlow = weatherResponse.asStateFlow()

    var isDataBaseEmpty:Boolean = true

    private var realm: Realm = Realm.getDefaultInstance()

    private var RepoObj = RepositoryClass()

    var city: String? = null
    var country: String? = null
    var key: String? = null
    var weatherDataResult: WeatherClass = WeatherClass(0, arrayListOf() )
    fun getWeatherData() {
        weatherResponse.value = WeatherRequest.Loading
        viewModelScope.launch {
            try {
                var WeatherObjectList = ArrayList<WeatherClass>()
                WeatherObjectList = RepoObj.getFromNetwokCall()
                weatherResponse.value = WeatherRequest.Success(WeatherObjectList)
                RepoObj.adddToDB(WeatherObjectList)
            } catch (e: Exception) {
                var history : MutableLiveData<List<WeatherModel>>
                history = RepoObj.getFromDB()

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
                }


            }
        }

    }

}