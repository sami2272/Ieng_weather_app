package com.smart.weatherapp.remote.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class WeatherResponse(
    @SerializedName("weather") var weather: ArrayList<Weather> = arrayListOf(),
    @SerializedName("main") var main: Main? = Main(),
    @SerializedName("id") var id: Int? = null,
    @SerializedName("name") var name: String? = null,
) : Parcelable



@Parcelize
data class Weather(

    @SerializedName("id") var id: Int? = null,
    @SerializedName("main") var main: String? = null,
    @SerializedName("description") var description: String? = null,
    @SerializedName("icon") var icon: String? = null

) : Parcelable


@Parcelize
data class Main(
    @SerializedName("temp") var temp: Float? = null,
) : Parcelable



