package com.smart.weatherapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.smart.weatherapp.data.CitiesDataSource
import com.smart.weatherapp.model.CitiesLocationResponse
import com.smart.weatherapp.utils.ApiResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class CitiesViewModel ( private val citiesDataSource: CitiesDataSource) : ViewModel() {

    val currentLocation =
        MutableStateFlow<ApiResult<ArrayList<CitiesLocationResponse>>>(ApiResult.Loading(null, true))

    suspend fun getLatLonByCity(cityName: String) {
        viewModelScope.launch {
            citiesDataSource.getLatLonByCity(cityName) {
                currentLocation.value = it
            }
        }
    }

    class Factory(
        private val locationDataSource: CitiesDataSource

    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return CitiesViewModel(locationDataSource) as T
        }
    }
}