package com.smart.weatherapp.remote.datasource

import android.util.Log
import com.smart.weatherapp.remote.api.WeatherApiService
import com.smart.weatherapp.remote.model.CityDataClass
import com.smart.weatherapp.remote.model.DetaialedWeather
import com.smart.weatherapp.remote.model.WeatherResponse
import com.smart.weatherapp.ui.actvities.MainActivity.Companion.cityDao
import com.smart.weatherapp.utils.ApiResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class WeatherRemoteDataSource(private val weatherApiService: WeatherApiService) {


    suspend fun getWeatherbyname(
        cityName: String,
        apikey: String,
        callback: (ApiResult<WeatherResponse>) -> Unit
    ) {


        weatherApiService.getCityWeather(cityName, apikey)
            .enqueue(object : Callback<WeatherResponse> {
                override fun onResponse(
                    call: Call<WeatherResponse>,
                    response: Response<WeatherResponse>
                ) {
                    callback(
                        if (response.isSuccessful) {
                            val currentWeatherResponse = response.body()
                            var respose = response.body() as WeatherResponse

                            GlobalScope.launch(Dispatchers.IO) {
                                cityDao.let {
                                    it?.insertCities(
                                        CityDataClass(
                                            respose.id.toString(),
                                            respose.name!!,
                                            respose.main!!.temp!!,
                                            respose.weather[0].main!!
                                        )
                                    )
                                }
                            }


                            Log.d("samiiii", "onResponse: ${currentWeatherResponse}")
                            ApiResult.Success(response.body())
                        } else {
                            ApiResult.Loading(
                                null,
                                true
                            )
                        }
                    )
                }

                override fun onFailure(call: Call<WeatherResponse>, t: Throwable) {
                    callback(ApiResult.Error(t.message.toString()))
                }
            })


    }

    suspend fun getdetailedWeather(
        cityName: String,
        apikey: String,
        callback: (ApiResult<DetaialedWeather>) -> Unit
    ) {


        weatherApiService.getCitydetailedWeather(cityName, apikey)
            .enqueue(object : Callback<DetaialedWeather> {
                override fun onResponse(
                    call: Call<DetaialedWeather>,
                    response: Response<DetaialedWeather>
                ) {
                    callback(
                        if (response.isSuccessful) {
                            val detailedWeatherResponse = response.body()
                            var respose = response.body() as DetaialedWeather
                            GlobalScope.launch(Dispatchers.IO) {
                                cityDao.let {
                                    it?.insert_weather_forecast(
                                        DetaialedWeather(
                                            respose.list,
                                            respose.city,
                                            respose.city.name
                                        )
                                    )
                                }
                            }

                            Log.d("samiiiidataa", "Dtaa: ${detailedWeatherResponse}")
                            ApiResult.Success(response.body())

                        } else {
                            ApiResult.Loading(
                                null,
                                true
                            )
                        }
                    )
                }

                override fun onFailure(call: Call<DetaialedWeather>, t: Throwable) {
                    callback(ApiResult.Error(t.message.toString()))
                }
            })


    }


}