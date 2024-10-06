package com.smart.weatherapp.remote.api


import com.smart.weatherapp.remote.model.DetaialedWeather
import com.smart.weatherapp.remote.model.WeatherResponse
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApiService {


    companion object {
        var weatherApiService: WeatherApiService? = null

        fun getInstance(baseurl:String): WeatherApiService {
            if (weatherApiService == null) {
                val retrofit = Retrofit.Builder()
                    .baseUrl(baseurl)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                weatherApiService = retrofit.create(WeatherApiService::class.java)
            }
            return weatherApiService!!
        }
    }

    @GET("weather")
     fun getCityWeather(
        @Query("q") cityName: String,
        @Query("appid") apiKey: String,
        @Query("units") metric: String="metric"
    ): Call<WeatherResponse>

    @GET("forecast")
    fun getCitydetailedWeather(
        @Query("q") cityName: String,
        @Query("appid") apiKey: String,
        @Query("units") metric: String="metric"
    ): Call<DetaialedWeather>


}