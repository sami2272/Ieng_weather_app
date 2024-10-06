package com.smart.weatherapp.remote.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize


@Entity(tableName = "foracast_table")
@Parcelize
data class DetaialedWeather(
    val list: ArrayList<ForecastItem>,
    val city: City,
    @PrimaryKey
    val name: String
): Parcelable

@Parcelize
data class ForecastItem(
    val dt: Long,
    val main: MainWeather,
    val weather: ArrayList<WeatherCondition>,
    val wind: Winddata,
    val dt_txt: String
): Parcelable

@Parcelize
data class MainWeather(
    val temp: Float,
    val temp_min: Float,
    val temp_max: Float,
    val humidity: Int
): Parcelable

@Parcelize
data class WeatherCondition(
    val description: String,
    val icon: String
): Parcelable


@Parcelize
data class Winddata(
    val speed: Float
): Parcelable

@Parcelize
data class City(
    val name: String,
    val country: String
): Parcelable
