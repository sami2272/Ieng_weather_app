package com.smart.weatherapp.remote.api

import com.smart.weatherapp.remote.model.CitiesLocationResponse
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface CitiesAPIService {

    @GET("direct")
    fun getCityData(
        @Query("q") cityName: String,
        @Query("limit") limit: Int = 5,
        @Query("appid") appID: String="a8904f4bd9f62c77bf981b593e558c12",
    ): Call<ArrayList<CitiesLocationResponse>>

    companion object {
        var locationApiService: CitiesAPIService? = null

        fun getInstance(baseURL:String): CitiesAPIService {
            if (locationApiService == null) {
                val retrofit = Retrofit.Builder()
                    .baseUrl(baseURL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                locationApiService = retrofit.create(CitiesAPIService::class.java)
            }
            return locationApiService!!
        }
    }
}