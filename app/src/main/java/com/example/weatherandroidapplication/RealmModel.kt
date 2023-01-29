package com.example.weatherandroidapplication
import com.example.weatherandroidapplication.models.WeatherClass
import io.realm.RealmModel
import io.realm.annotations.PrimaryKey
import io.realm.annotations.RealmClass
import io.realm.annotations.Required
@RealmClass
open class WeatherModel : RealmModel {
    @PrimaryKey
    var id:String? = ""
    @Required
    var city:String? = ""
    
    var temp:Int = 0
    @Required
    var description:String? = ""

}