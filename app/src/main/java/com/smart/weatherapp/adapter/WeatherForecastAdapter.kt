package com.smart.weatherapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.smart.weatherapp.databinding.ItemWeatherdetailsBinding
import com.smart.weatherapp.model.ForecastItem
import com.smart.weatherapp.utils.ExtentionFuctions.getWeatherDrawable
import com.smart.weatherapp.utils.ExtentionFuctions.toDayOfWeek
import com.smart.weatherapp.utils.ExtentionFuctions.toRoundedInt

class WeatherForecastAdapter(private val forecastdetails: List<ForecastItem>) :
    RecyclerView.Adapter<WeatherForecastAdapter.ViewHolder>() {

    class ForecastWeatherViewModel(forecast: ForecastItem) {
        val dayofweek = forecast.dt_txt.toDayOfWeek()
        val min = "${forecast.main.let { it?.temp_min.let { it?.toRoundedInt() }}}°C"
        val max = "${forecast.main.let { it?.temp_max.let { it?.toRoundedInt() } }}°C"
        val cityCondion = forecast.weather[0].description

    }

    inner class ViewHolder(private val binding: ItemWeatherdetailsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(forecast: ForecastItem) {
            binding.forecastModel = ForecastWeatherViewModel(forecast)
            binding.tvCondition.setSelected(true)
            binding.imgCondition.setImageResource(forecastdetails[position].weather[0].description.getWeatherDrawable(binding.root.context))

        }

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemWeatherdetailsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(forecastdetails[position])

    }

    override fun getItemCount() = forecastdetails.size
}
