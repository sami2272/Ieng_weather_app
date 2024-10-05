package com.smart.weatherapp.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.smart.weatherapp.utils.InternetConnectionLiveData

class InternetViewModel(private val context: Context) : ViewModel() {
    private val _isConnected = MutableLiveData<Boolean>()
    val isConnected: LiveData<Boolean> get() = _isConnected

    private val networkConnectionLiveData = InternetConnectionLiveData(context)

    init {
        networkConnectionLiveData.observeForever { connection ->
            _isConnected.value = connection // Update LiveData with connection status
        }
    }

    fun checkInternetConnection() {
        _isConnected.value = networkConnectionLiveData.isConnected() // This will check the connection
    }

    // ViewModel Factory
    class Factory(private val context: Context) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(InternetViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return InternetViewModel(context) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}
