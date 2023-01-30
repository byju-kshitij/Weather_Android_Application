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

    @Required
    var sunrise : String? = ""

    @Required
    var sunset : String? = ""

    @Required
    var humidity : Int? = 0

    @Required
    var visibility : Int? = 0

    @Required
    var clouds : Int? = 0

    @Required
    var winds : Double? = 0.0

    @Required
    var pressure : Double? = 0.0

}