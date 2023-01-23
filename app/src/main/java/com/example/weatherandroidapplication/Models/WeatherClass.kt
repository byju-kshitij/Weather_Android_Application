package com.example.weatherandroidapplication.Models

import com.squareup.moshi.Json
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
    val data: List<Data>
)