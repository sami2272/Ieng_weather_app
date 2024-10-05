package com.smart.weatherapp.roomDb

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.smart.weatherapp.dao.CityDao
import com.smart.weatherapp.model.CityDataClass
import com.smart.weatherapp.model.DetaialedWeather
import com.smart.weatherapp.typeconverters.Converters


@Database(entities = [CityDataClass::class,DetaialedWeather::class], version = 1)
@TypeConverters(Converters::class)
abstract class WeatherDB : RoomDatabase() {
    abstract fun cityDao(): CityDao

    companion object {
        @Volatile
        private var INSTANCE: WeatherDB? = null
        suspend fun getDatabase(context: Context): WeatherDB {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    WeatherDB::class.java,
                    "weather_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}

