package com.smart.weatherapp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.smart.weatherapp.R
import com.smart.weatherapp.adapter.CitiesAdapter
import com.smart.weatherapp.databinding.FragmentCitiesListBinding
import com.smart.weatherapp.databinding.FragmentCityPickerBinding
import com.smart.weatherapp.model.CitiesLocationResponse

private const val ARG_PARAM1 = "param1"
class CityPickerFragment : DialogFragment() {

    private val binding by lazy {
        FragmentCityPickerBinding.inflate(layoutInflater)
    }


    private var list: ArrayList<CitiesLocationResponse>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isCancelable = false
        arguments?.let {
            list = it.getSerializable(ARG_PARAM1) as ArrayList<CitiesLocationResponse>
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.imgCancel.setOnClickListener { v: View? -> dismiss() }
        setAdapter()

        binding.back.setOnClickListener { this.dismiss() }
    }

    private fun setAdapter() {
        list?.let {
            if (it.size>0){
                binding.txtno.visibility = View.GONE
            }
            val adapter = CitiesAdapter(it) {
                mClickListener(it)
                dismiss()
            }
            binding.recyclerCity.layoutManager = LinearLayoutManager(requireContext())
            binding.recyclerCity.adapter = adapter
        }

    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        dialog?.window?.decorView?.setBackgroundResource(android.R.color.transparent)
    }

    companion object {

        lateinit var mClickListener: (geoCodingResponse: CitiesLocationResponse) -> Unit

        @JvmStatic
        fun newInstance(
            list: ArrayList<CitiesLocationResponse>,
            clickListener: (geoCodingResponse: CitiesLocationResponse) -> Unit
        ) =
            CityPickerFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(ARG_PARAM1, list)
                }
                mClickListener = clickListener
            }
    }

}