package com.smart.weatherapp.utils

import android.content.Context
import com.smart.weatherapp.R
import java.text.SimpleDateFormat
import java.util.Locale
import kotlin.math.roundToInt

object ExtentionFuctions {


    fun String.toDayOfWeek(): String {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val date = inputFormat.parse(this)
        val outputFormat = SimpleDateFormat("EEEE", Locale.getDefault())
        return outputFormat.format(date)
    }


    fun Float.toRoundedInt(): Int {
        return this.roundToInt()
    }

    fun String.getWeatherDrawable(context: Context): Int {
        return when {
            this.contains("rain", ignoreCase = true) -> R.drawable.rain
            this.contains("cloud", ignoreCase = true) -> R.drawable.cloudy
            this.contains("thunder", ignoreCase = true) -> R.drawable.thunder
            else -> R.drawable.sunny_cloudy
        }
    }
}