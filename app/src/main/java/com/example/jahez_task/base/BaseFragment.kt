package com.example.jahez_task.base

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.jahez_task.R
import com.google.android.material.snackbar.Snackbar

open class BaseFragment: Fragment() {

    fun showSnackbar(view: View, message: String){
        Snackbar.make(view, message, Snackbar.LENGTH_LONG)
            .setBackgroundTint(
                resources.getColor(R.color.light_red, resources.newTheme())
            )
            .setTextColor(
                resources.getColor(R.color.white, resources.newTheme())
            ).show()
    }

    fun popBackStack(){
        findNavController().popBackStack()
    }
}