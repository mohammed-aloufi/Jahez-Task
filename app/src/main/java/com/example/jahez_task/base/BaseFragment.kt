package com.example.jahez_task.base

import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.*
import androidx.navigation.fragment.findNavController
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
        result: suspend (T) -> Unit
    ) {
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                flow.collectLatest(result)
            }
        }
    }

    fun <T> collectLifecycleFlow(
        viewLifecycleOwner: LifecycleOwner,
        flow: Flow<T>,
        result: suspend (T) -> Unit
    ) {
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.CREATED) {
                flow.collect(result)
            }
        }
    }
}