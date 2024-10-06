package com.smart.weatherapp.local.typeconverters

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.smart.weatherapp.remote.model.City
import com.smart.weatherapp.remote.model.ForecastItem

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
    fun fromCity(city: City?): String {
        return Gson().toJson(city)
    }


    @TypeConverter
    fun toCity(cityString: String): City {
        return Gson().fromJson(cityString, City::class.java)
    }

}
