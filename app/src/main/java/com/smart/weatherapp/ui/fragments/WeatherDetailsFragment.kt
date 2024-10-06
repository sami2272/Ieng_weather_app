package com.smart.weatherapp.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.smart.weatherapp.R
import com.smart.weatherapp.databinding.FragmentWeatherDetailsBinding
import com.smart.weatherapp.remote.api.WeatherApiService
import com.smart.weatherapp.remote.datasource.WeatherRemoteDataSource
import com.smart.weatherapp.remote.model.ForecastItem
import com.smart.weatherapp.remote.viewmodel.WeatherViewModel
import com.smart.weatherapp.ui.adapter.WeatherForecastAdapter
import com.smart.weatherapp.utils.ApiStatus
import com.smart.weatherapp.utils.ExtentionFuctions.getWeatherDrawable
import com.smart.weatherapp.utils.ExtentionFuctions.toRoundedInt
import com.smart.weatherapp.utils.NoInternetDialog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class WeatherDetailsFragment : Fragment() {
    private lateinit var forecastWeatherAdapter: WeatherForecastAdapter
    private val binding by lazy {
        FragmentWeatherDetailsBinding.inflate(layoutInflater)
    }


    private val weatherViewModel: WeatherViewModel by lazy {
        ViewModelProvider(
            this,
            WeatherViewModel.Factory(
                WeatherRemoteDataSource(WeatherApiService.getInstance(requireContext().getString(R.string.weather_url)))
            )
        )[WeatherViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            handleOnBackPressed(view)
        }
        binding.backBtn.setOnClickListener {
            handleOnBackPressed(view)
        }
        var city = arguments?.getString("cityName")
        binding.textView.text = city
        lifecycleScope.launch(Dispatchers.IO) {
            city?.let { fetchweather(it) }
        }

    }

    suspend fun fetchweather(city: String) {
        lifecycleScope.launch(Dispatchers.IO) {
            city.let { it1 ->
                weatherViewModel.getWeatherdetails(
                    it1,
                    "a8904f4bd9f62c77bf981b593e558c12"
                )
            }

        }


        lifecycleScope.launch {
            weatherViewModel.detailedWeather.collectLatest {
                when (it.status) {

                    ApiStatus.SUCCESS -> {
                        it.data?.let { detailedweatherResponse ->
                            withContext(Dispatchers.Main) {
                                binding.loading.visibility = View.GONE
                                binding.layoutdata.visibility = View.VISIBLE
                                Log.d("samiiisss", "Dtaa: ${detailedweatherResponse.list.size}")
                            }
                            setupUI(detailedweatherResponse.list)
                        }
                    }

                    ApiStatus.ERROR -> {
                        withContext(Dispatchers.Main) {
                            weatherViewModel.getForecastForCityfromDB(city)
                                .observe(viewLifecycleOwner) { forecastList ->
                                    forecastList?.let { data ->
                                        lifecycleScope
                                            .launch(Dispatchers.IO) {
                                                setupUI(data.list)
                                            }
                                        if (data.list.size > 0) {
                                           var dislog= NoInternetDialog("An Error Occured", "${it.message}") {
                                                // Retry checking the connection
                                                lifecycleScope
                                                    .launch(Dispatchers.IO) {
                                                        city.let { it1 ->
                                                            weatherViewModel.getWeatherdetails(
                                                                it1,
                                                                "a8904f4bd9f62c77bf981b593e558c12"
                                                            )
                                                        }
                                                    }

                                            }
                                            dislog?.show(requireActivity().supportFragmentManager, "NoInternetDialog")
                                            Toast.makeText(
                                                requireContext(),
                                                "Error",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                            binding.loading.visibility = View.GONE
                                            binding.layoutdata.visibility = View.VISIBLE
                                        }
                                        Log.d("getdata", "sdss: ${data.name}")
                                    }
                                }

                        }
                    }

                    ApiStatus.LOADING -> {
                        withContext(Dispatchers.Main) {
                            binding.loading.visibility = View.VISIBLE
                            binding.layoutdata.visibility = View.GONE
                        }

                    }
                }
            }
        }
    }

    private fun handleOnBackPressed(view: View) {
        if (view.findNavController().previousBackStackEntry != null) {
            view.findNavController().popBackStack()
        } else {
            requireActivity().finish()
        }
    }

    suspend fun setupUI(list: ArrayList<ForecastItem>) {

        lifecycleScope.launch(Dispatchers.IO) {
            var filteredList = getDailyForecast(list)
            forecastWeatherAdapter = WeatherForecastAdapter(filteredList)
            withContext(Dispatchers.Main) {
                binding.tvWindSpeed.text = filteredList[0].wind.speed.toString()
                binding.tvHumidity.text = filteredList[0].main.humidity.toString()
                binding.tvHumidity.text = filteredList[0].main.humidity.toString()
                binding.tvTemperaturemin.text = "${list[0].main.temp_min.toRoundedInt()}°"
                binding.tvTemperaturemax.text = "${list[0].main.temp_max.toRoundedInt()}°"
                binding.imgToday.setImageResource(
                    filteredList[0].weather[0].description.getWeatherDrawable(
                        binding.root.context
                    )
                )
                binding.rvForecast.layoutManager = LinearLayoutManager(requireContext())
                binding.rvForecast.adapter = forecastWeatherAdapter
            }

        }
    }

    fun getDailyForecast(forecastResponse: ArrayList<ForecastItem>): ArrayList<ForecastItem> {
        val dailyForecast = arrayListOf<ForecastItem>()
        val addedDates = mutableSetOf<String>()
        forecastResponse.forEach {
            val date = it.dt_txt.split(" ")[0]
            if (!addedDates.contains(date) && it.dt_txt.contains("12:00:00")) {
                dailyForecast.add(it)
                addedDates.add(date)
            }
        }
        return dailyForecast
    }

}