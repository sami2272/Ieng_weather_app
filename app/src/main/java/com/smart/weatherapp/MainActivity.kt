package com.smart.weatherapp

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.smart.weatherapp.dao.CityDao
import com.smart.weatherapp.roomDb.WeatherDB
import com.smart.weatherapp.utils.NoInternetDialog
import com.smart.weatherapp.viewmodel.InternetViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private var noInternetDialog: NoInternetDialog? = null
    private val networkViewModel: InternetViewModel by viewModels {
        InternetViewModel.Factory(applicationContext) // Pass application context to the factory
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        lifecycleScope.launch(Dispatchers.IO) {
            cityDao = WeatherDB.getDatabase(applicationContext).cityDao()
        }

        networkViewModel.isConnected.observe(this, Observer { isConnected ->
            if (isConnected) {
                // Internet is available
                // Update UI accordingly
                noInternetDialog?.dismiss()
            } else {
                if (noInternetDialog == null) {
                    noInternetDialog = NoInternetDialog("No Internet Connection","Please check your internet connection and try again.") {
                        // Retry checking the connection
                        networkViewModel.checkInternetConnection()
                    }
                    noInternetDialog?.show(supportFragmentManager, "NoInternetDialog")
                }
            }
        })
    }
    companion object{
        var cityDao: CityDao?= null
    }

}