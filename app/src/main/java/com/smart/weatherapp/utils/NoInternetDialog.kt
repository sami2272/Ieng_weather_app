package com.smart.weatherapp.utils

import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment

class NoInternetDialog(var title :String,var desc:String,private val retryCallback: () -> Unit) : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): AlertDialog {
        return AlertDialog.Builder(requireContext())
            .setTitle(title)
            .setMessage(desc)
            .setPositiveButton("Retry") { dialog, _ ->
                retryCallback() // Call the retry function
                dialog.dismiss()
            }
            .setNegativeButton("Cancel") { dialog, _ -> dialog.dismiss() }
            .create()
    }
}
