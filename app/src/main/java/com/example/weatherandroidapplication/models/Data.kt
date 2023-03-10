package com.example.weatherandroidapplication.models

import WeatherX

data class Data(
    val app_temp2: Double = 0.0,
    val aqi: Int = 0,
    var city_name: String = "",
    var clouds: Int = 0,
    val country_code: String = "",
    val datetime: String = "",
    val dewpt: Double = 0.0,
    val dhi: Double = 0.0,
    val dni: Double= 0.0,
    val elev_angle: Double= 0.0,
    val ghi: Double= 0.0,
    val gust: Any= 0,
    val h_angle: Int = 0,
    val lat: Double= 0.0,
    val lon: Double= 0.0,
    var ob_time: String = "",
    val pod: String = "",
    val precip: Any= 0,
    var pres: Double= 0.0,
    var rh: Int= 0,
    val slp: Int= 0,
    val snow: Any= 0,
    val solar_rad: Double= 0.0,
    val sources: List<String> = listOf(),
    val state_code: String = "",
    val station: String = "",
    var sunrise: String = "",
    var sunset: String = "",
    var temp: Int = 0,
    val timezone: String = "",
    val ts: Double= 0.0,
    val uv: Double= 0.0,
    var vis: Int= 0,
    val weather: WeatherX = WeatherX(0,"",""),
    val wind_cdir: String = "",
    val wind_cdir_full: String = "",
    val wind_dir: Int = 0,
    var wind_spd: Double= 0.0,
)