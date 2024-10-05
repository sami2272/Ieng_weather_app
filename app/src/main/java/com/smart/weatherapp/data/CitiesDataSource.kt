package com.smart.weatherapp.data

import android.util.Log
import com.smart.weatherapp.api.CitiesAPIService
import com.smart.weatherapp.model.CitiesLocationResponse
import com.smart.weatherapp.utils.ApiResult
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CitiesDataSource(private val locationApiService: CitiesAPIService) {

    suspend  fun getLatLonByCity(
        cityName: String,
        callback: (ApiResult<ArrayList<CitiesLocationResponse>>) -> Unit
    ) {
        callback(ApiResult.Loading(null, true))
        locationApiService.getCityData(cityName)
            .enqueue(object : Callback<ArrayList<CitiesLocationResponse>> {
                override fun onResponse(
                    call: Call<ArrayList<CitiesLocationResponse>>,
                    response: Response<ArrayList<CitiesLocationResponse>>
                ) {
                    Log.e("TESTTAG", "CALLED OK ${response.isSuccessful}")
                    callback(
                        if (response.isSuccessful)

                            ApiResult.Success(response.body())
                        else
                            ApiResult.Error(
                                response.message(),
                            )
                    )
                }

                override fun onFailure(call: Call<ArrayList<CitiesLocationResponse>>, t: Throwable) {
                    Log.e("TESTTAG", "CALLED Failed ${t.message}")
                    callback(ApiResult.Error(t.message.toString()))
                }

            })
    }

}