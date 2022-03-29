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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

open class BaseFragment : Fragment() {

    private lateinit var mBaseViewModel: BaseViewModel

    protected fun setBaseViewModel(baseViewModel: BaseViewModel) {
        mBaseViewModel = baseViewModel
        collectLifecycleFlow(viewLifecycleOwner, mBaseViewModel.state) { state ->
            when {
                state.isLoading -> {
                    showProgress(true)
                }
                state.message.isNotBlank() -> {
                    showErrorMsg(true, state.message)
                    showSnackbar(requireView(), state.message)
                    showProgress(false)
                }
                else -> {
                    showProgress(false)
//                    showErrorMsg(false)
                }
            }
        }
    }

    private fun showProgress(show: Boolean) {
        val base = requireActivity() as MainActivity
        base.showProgress(show)
    }

    private fun showErrorMsg(show: Boolean, message: String = "") {
        val base = requireActivity() as MainActivity
        base.showErrorMsg(show, message)
    }

    private fun showSnackbar(view: View, message: String){
        val base = requireActivity() as MainActivity
        base.showSnackbar(view, message)
    }


    fun popBackStack() {
        findNavController().popBackStack()
    }

    fun <T> collectLatestLifecycleFlow(
        viewLifecycleOwner: LifecycleOwner,
        flow: Flow<T>,
        collect: suspend (T) -> Unit
    ) {
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                flow.collectLatest(collect)
            }
        }
    }

    fun <T> collectLifecycleFlow(
        viewLifecycleOwner: LifecycleOwner,
        flow: Flow<T>,
        collect: suspend (T) -> Unit
    ) {
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.CREATED) {
                flow.collect(collect)
            }
        }
    }
}