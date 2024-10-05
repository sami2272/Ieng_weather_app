package com.smart.weatherapp.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable


@Entity(tableName = "Foracst_Table")
data class DetaialedWeather(
    val list: ArrayList<ForecastItem>,
    val city: City,
    @PrimaryKey
    val name: String
): Serializable

data class ForecastItem(
    val dt: Long,
    val main: MainWeather,
    val weather: ArrayList<WeatherCondition>,
    val wind: Winddata,
    val dt_txt: String
): Serializable

data class MainWeather(
    val temp: Float,
    val temp_min: Float,
    val temp_max: Float,
    val humidity: Int
): Serializable

data class WeatherCondition(
    val description: String,
    val icon: String
): Serializable

data class Winddata(
    val speed: Float
): Serializable

data class City(
    val name: String,
    val country: String
): Serializable
