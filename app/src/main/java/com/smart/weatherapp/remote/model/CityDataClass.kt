package com.smart.weatherapp.remote.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity
@Parcelize
data class CityDataClass(
    @PrimaryKey
    val id: String,
    val name: String,
    val temperature: Float,
    val weatherCondition: String
): Parcelable