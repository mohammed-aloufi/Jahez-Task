package com.example.jahez_task.base

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.jahez_task.R
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

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

    fun <T> collectLatestLifecycleFlow(viewLifecycleOwner: LifecycleOwner, flow: Flow<T>, collect: suspend (T) -> Unit) {
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                flow.collectLatest(collect)
            }
        }
    }

    fun <T> collectLifecycleFlow(viewLifecycleOwner: LifecycleOwner, flow: Flow<T>, collect: suspend (T) -> Unit) {
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.CREATED) {
                flow.collect(collect)
            }
        }
    }
}