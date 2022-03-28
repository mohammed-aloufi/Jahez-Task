package com.example.jahez_task.base

import android.util.Log
import androidx.lifecycle.*
import com.example.jahez_task.domain.models.Resource
import com.example.jahez_task.domain.models.State
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
open class BaseViewModel @Inject constructor(): ViewModel() {

    private val _state : MutableStateFlow<State> = MutableStateFlow(State())
    val state : StateFlow<State> = _state

    fun <T> collectFlow(flow: Flow<T>, data: (T) -> Unit){
        viewModelScope.launch {
            flow.catch{ err ->
                _state.emit(State(message = err.message.toString()))
            }.collect { result ->
                when(result){
                    is Resource.Loading<*> -> {
                        _state.emit(State(isLoading = true))
                    }
                    is Resource.Error<*> -> {
                        _state.emit(State(message = result.message ?: ""))
                    }
                    is Resource.Success<*> -> {
                        _state.emit(State())
                        data(result)
                    }
                }
            }
        }
    }
}