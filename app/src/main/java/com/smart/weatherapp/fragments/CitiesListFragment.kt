package com.smart.weatherapp.fragments

import android.os.Bundle
import android.os.Handler
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.smart.weatherapp.MainActivity.Companion.cityDao
import com.smart.weatherapp.R
import com.smart.weatherapp.adapter.CityWeatherAdapter
import com.smart.weatherapp.api.CitiesAPIService
import com.smart.weatherapp.api.WeatherApiService
import com.smart.weatherapp.data.CitiesDataSource
import com.smart.weatherapp.data.WeatherRemoteDataSource
import com.smart.weatherapp.databinding.FragmentCitiesListBinding
import com.smart.weatherapp.utils.ApiStatus
import com.smart.weatherapp.utils.NoInternetDialog
import com.smart.weatherapp.viewmodel.CitiesViewModel
import com.smart.weatherapp.viewmodel.WeatherViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class CitiesListFragment : Fragment() {
    private var isDialogShown = false
    private lateinit var cityWeatherAdapter: CityWeatherAdapter
    var itemview: View? = null
    private val locationViewModel: CitiesViewModel by lazy {
        ViewModelProvider(
            requireActivity(),
            CitiesViewModel.Factory(
                CitiesDataSource(CitiesAPIService.getInstance(requireContext().getString(R.string.baseurl))),
            )
        )[CitiesViewModel::class.java]
    }
    private val weatherViewModel: WeatherViewModel by lazy {
        ViewModelProvider(
            this,
            WeatherViewModel.Factory(
                WeatherRemoteDataSource(WeatherApiService.getInstance(requireContext().getString(R.string.weather_url)))
            )
        )[WeatherViewModel::class.java]
    }
    private val binding by lazy {
        FragmentCitiesListBinding.inflate(layoutInflater)
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

        itemview = view

        Handler().postDelayed({
            lifecycleScope.launch(Dispatchers.IO) {
                setupUI()
            }
        }, 2000)

        binding.edtSearch.setOnKeyListener(object : View.OnKeyListener {
            override fun onKey(v: View?, keyCode: Int, event: KeyEvent): Boolean {
                if ((event.action == KeyEvent.ACTION_DOWN) &&
                    (keyCode == KeyEvent.KEYCODE_ENTER)
                ) {
                    searchCity()
                    return true
                }
                return false
            }
        })

        binding.imgSearch.setOnClickListener {
            searchCity()
        }

        binding.refreshRv.setOnRefreshListener {
            cityWeatherAdapter = CityWeatherAdapter(emptyList()) {}
            cityWeatherAdapter.notifyDataSetChanged()
            lifecycleScope.launch(Dispatchers.IO) {
                var cities = cityDao.let { it!!.getAllCities() }
                cities.forEach {
                    fetchweather(it.name)
                }
            }

        }

        lifecycleScope.launch {

            locationViewModel.currentLocation.collectLatest {
                if (it.data?.size == 1) {
                    it.data[0].name?.let { it1 -> fetchweather(it1) }
                    return@collectLatest
                }
                val fragment =
                    it.data?.let { it1 ->
                        CityPickerFragment.newInstance(it1) { cities ->
                            lifecycleScope.launch {
                                cities.name?.let { it2 -> fetchweather(it2) }
                            }
                        }
                    }
                fragment?.show(requireActivity().supportFragmentManager, "city_picker")

            }

        }

    }


    suspend fun fetchweather(city: String) {

        lifecycleScope.launch(Dispatchers.IO) {
            city.let { it1 ->
                weatherViewModel.getWeather(
                    it1,
                    "a8904f4bd9f62c77bf981b593e558c12"
                )

            }
        }


        lifecycleScope.launch {
            weatherViewModel.currentWeather.collectLatest {
                when (it.status) {

                    ApiStatus.SUCCESS -> {
                        binding.progess.visibility = View.GONE
                        it.data?.let { weatherResponse ->
                            setupUI()
                        }
                    }

                    ApiStatus.ERROR -> {
                        if (!isDialogShown) { // Check if the dialog is not shown
                            isDialogShown = true // Set the flag to true

                            var dislog = NoInternetDialog("An Error Occured", "${it.message}") {
                                cityWeatherAdapter = CityWeatherAdapter(emptyList()) {}
                                cityWeatherAdapter.notifyDataSetChanged()
                                lifecycleScope.launch(Dispatchers.IO) {
                                    var cities = cityDao.let { it!!.getAllCities() }
                                    cities.forEach {
                                        fetchweather(it.name)
                                    }
                                    isDialogShown = false
                                }
                            }
                            dislog?.show(
                                requireActivity().supportFragmentManager,
                                "NoInternetDialog"
                            )
                            binding.progess.visibility = View.GONE
                            binding.refreshRv.isRefreshing = false

                        }
                    }

                    ApiStatus.LOADING -> {
                        binding.progess.visibility = View.VISIBLE
                    }
                }
            }
        }
    }


    suspend fun setupUI() {
        binding.refreshRv.isRefreshing = false
        lifecycleScope.launch(Dispatchers.IO) {
            var cities = cityDao.let { it!!.getAllCities() }

            cityWeatherAdapter = CityWeatherAdapter(cities) { data ->

                itemview.let {
                    findNavController(it!!).navigate(
                        R.id.action_cityListFragment_to_weatherDetailFragment,
                        bundleOf("cityName" to data.name)
                    )
                }
            }
            withContext(Dispatchers.Main) {
                when (cities.size) {
                    0 -> {
                        binding.tvlocation.visibility = View.VISIBLE
                    }

                    else -> {
                        binding.tvlocation.visibility = View.GONE
                    }
                }
                binding.progess.visibility = View.GONE
                binding.rvCities.layoutManager = LinearLayoutManager(requireContext())
                binding.rvCities.adapter = cityWeatherAdapter
            }
        }
    }

    fun searchCity() {
        when (binding.edtSearch.text.toString()) {
            "" -> {
                Toast.makeText(requireContext(), "Enter Text to Search", Toast.LENGTH_SHORT).show()
                return
            }

            else -> {
                lifecycleScope.launch(Dispatchers.IO) {
                    locationViewModel.getLatLonByCity(binding.edtSearch.text.toString())
                }
            }
        }
        binding.edtSearch.clearFocus()

    }


}