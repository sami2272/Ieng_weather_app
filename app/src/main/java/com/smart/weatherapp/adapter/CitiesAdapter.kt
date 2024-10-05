package com.smart.weatherapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.smart.weatherapp.databinding.ListItemCityBinding
import com.smart.weatherapp.model.CitiesLocationResponse


class CitiesAdapter(private val list: ArrayList<CitiesLocationResponse>, val callback: (CitiesLocationResponse) -> Unit) :
    RecyclerView.Adapter<CitiesAdapter.ViewHolder>() {

    class CityViewModel(geoCoding: CitiesLocationResponse) {

        val cityWithCountry = "${geoCoding.name.toString()}, ${geoCoding.country.toString()}"

    }

    inner class ViewHolder(private val binding: ListItemCityBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(city: CitiesLocationResponse) {
            binding.viewModel  = CityViewModel(city)
            binding.root.setOnClickListener {
                callback(city)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ListItemCityBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }
}