package com.smart.weatherapp.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.smart.weatherapp.databinding.CitiesRvItemLayoutBinding
import com.smart.weatherapp.remote.model.CityDataClass
import com.smart.weatherapp.utils.ExtentionFuctions.getWeatherDrawable
import com.smart.weatherapp.utils.ExtentionFuctions.toRoundedInt

class CityWeatherAdapter(val cities: List<CityDataClass>, val callback: (CityDataClass) -> Unit) :
    RecyclerView.Adapter<CityWeatherAdapter.ViewHolder>() {


    inner class ViewHolder(private val binding: CitiesRvItemLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(weather: CityDataClass) {
            binding.tvCityName.text = weather.name
            binding.tvTemperature.text = "${weather.temperature.let { it?.toRoundedInt() }}°C"
            binding.tvConditionName.text = "${weather.weatherCondition}"
            binding.root.setOnClickListener {
                callback(weather)
            }
            binding.imgWeatherCondition.setImageResource(
                weather.weatherCondition.getWeatherDrawable(
                    binding.root.context
                )
            )

        }

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            CitiesRvItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(cities[position])
        //   holder.weatherIcon.setImageResource(cityWeather.weatherIconResId) // Set weather condition icon
    }

    override fun getItemCount() = cities.size
}
