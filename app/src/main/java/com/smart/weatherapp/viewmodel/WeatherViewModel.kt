package com.smart.weatherapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.smart.weatherapp.MainActivity
import com.smart.weatherapp.data.WeatherRemoteDataSource
import com.smart.weatherapp.model.DetaialedWeather
import com.smart.weatherapp.model.WeatherResponse
import com.smart.weatherapp.utils.ApiResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class WeatherViewModel constructor(
    private val weatherRemoteDataSource: WeatherRemoteDataSource
) : ViewModel() {

    val currentWeather =
        MutableStateFlow<ApiResult<WeatherResponse>>(ApiResult.Loading(null, true))


    val detailedWeather =
        MutableStateFlow<ApiResult<DetaialedWeather>>(ApiResult.Loading(null, true))

    suspend fun getWeather(cityName: String, apikey: String) {
        viewModelScope.launch {
            weatherRemoteDataSource.getWeatherbyname(cityName,apikey) {
                currentWeather.value = it
            }
        }
    }
    suspend fun getWeatherdetails(cityName: String, apikey: String) {
        viewModelScope.launch {
            weatherRemoteDataSource.getdetailedWeather(cityName,apikey) {
                detailedWeather.value = it
            }
        }
    }

    fun getForecastForCityfromDB(cityName: String): LiveData<DetaialedWeather> {
        return MainActivity.cityDao.let {it?.get_weather_forecast(cityName)!! }
    }


    class Factory(
        private val remote: WeatherRemoteDataSource

    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return WeatherViewModel(remote) as T
        }
    }
}