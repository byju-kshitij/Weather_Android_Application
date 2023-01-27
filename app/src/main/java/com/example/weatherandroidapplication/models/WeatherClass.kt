package com.example.weatherandroidapplication.models

//data class WeatherClass(
//    @field:Json(name = "count")
//    val count:Int ,
//    @field:Json(name = "data")
//    val data:String
//)

data class WeatherClass(
    //@field:Json(name = "count")
    val count: Int,
    //@field:Json(name = "data")
    val data: ArrayList<Data>
)