package com.smart.weatherapp.utils

import android.content.Context
import android.net.ConnectivityManager
import androidx.lifecycle.LiveData

class InternetConnectionLiveData(private val context: Context) : LiveData<Boolean>() {
    private val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    override fun onActive() {
        super.onActive()
        value = isConnected()
    }

    fun isConnected(): Boolean {
        val activeNetwork = connectivityManager.activeNetworkInfo
        return activeNetwork?.isConnectedOrConnecting == true
    }


}

