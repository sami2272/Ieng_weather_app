package com.smart.weatherapp.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.smart.weatherapp.model.CityDataClass
import com.smart.weatherapp.model.DetaialedWeather

@Dao
interface CityDao {

    @Query("SELECT * FROM CityDataClass")
    fun getAllCities(): List<CityDataClass>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCities(cities: CityDataClass)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert_weather_forecast(cities: DetaialedWeather)

    @Query("SELECT * FROM Foracst_Table WHERE name = :city_name")
    fun get_weather_forecast(city_name:String):LiveData<DetaialedWeather>
}
