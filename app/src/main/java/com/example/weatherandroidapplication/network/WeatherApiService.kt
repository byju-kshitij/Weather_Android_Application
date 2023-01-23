package com.example.weatherandroidapplication.network
import com.example.weatherandroidapplication.Models.WeatherClass
import com.squareup.moshi.Moshi
import retrofit2.Response

import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query



private const val BASE_URL =
    "https://api.weatherbit.io/"

private val retrofit = Retrofit.Builder()
    .addConverterFactory(GsonConverterFactory.create())
    .baseUrl(BASE_URL)
    .build()

//"city=deoghar&country=IN&key=9fcb60c4b3984edb99886da0d26b8ee7"

interface WeatherApiService {

    @GET("v2.0/current")
    suspend fun getWeatherdata(
        @Query("city") city: String,
        @Query("country") country: String,
        @Query("key") key: String
    ): WeatherClass

    companion object {
        var apiService: WeatherApiService? = null
        fun getInstance(): WeatherApiService {
            if (apiService == null) {
                apiService = Retrofit.Builder()
                    .baseUrl("https://api.weatherbit.io/")
                    .addConverterFactory(MoshiConverterFactory.create())
                    .build().create(WeatherApiService::class.java)
            }

            return apiService!!
        }
    }
}


object WeatherApi {
    val retrofitService : WeatherApiService by lazy {
        retrofit.create(WeatherApiService::class.java)
    }

}