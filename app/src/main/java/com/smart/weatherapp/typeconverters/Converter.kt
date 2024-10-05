package com.smart.weatherapp.typeconverters

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.smart.weatherapp.model.City
import com.smart.weatherapp.model.ForecastItem
import com.smart.weatherapp.model.MainWeather
import com.smart.weatherapp.model.WeatherCondition
import com.smart.weatherapp.model.Winddata

class Converters {


    @TypeConverter
    fun fromForecastList(value: ArrayList<ForecastItem>?): String {
        val gson = Gson()
        return gson.toJson(value)
    }


    @TypeConverter
    fun toForecastList(value: String): ArrayList<ForecastItem> {
        val listType = object : TypeToken<ArrayList<ForecastItem>>() {}.type
        return Gson().fromJson(value, listType)
    }


    @TypeConverter
    fun fromWeatherConditionList(value: ArrayList<WeatherCondition>?): String {
        val gson = Gson()
        return gson.toJson(value)
    }


    @TypeConverter
    fun toWeatherConditionList(value: String): ArrayList<WeatherCondition> {
        val listType = object : TypeToken<ArrayList<WeatherCondition>>() {}.type
        return Gson().fromJson(value, listType)
    }


    @TypeConverter
    fun fromCity(city: City?): String {
        return Gson().toJson(city)
    }


    @TypeConverter
    fun toCity(cityString: String): City {
        return Gson().fromJson(cityString, City::class.java)
    }

    @TypeConverter
    fun fromMainWeatherCity(city: MainWeather?): String {
        return Gson().toJson(city)
    }


    @TypeConverter
    fun toMainWeatherCity(cityString: String): MainWeather {
        return Gson().fromJson(cityString, MainWeather::class.java)
    }

    @TypeConverter
    fun fromWinddata(city: Winddata?): String {
        return Gson().toJson(city)
    }

    @TypeConverter
    fun toWinddata(cityString: String): Winddata {
        return Gson().fromJson(cityString, Winddata::class.java)
    }
}
