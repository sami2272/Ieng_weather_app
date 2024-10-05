package com.smart.weatherapp.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class CityDataClass(
    @PrimaryKey
    val id: String,
    val name: String,
    val temperature: Float,
    val weatherCondition: String
)