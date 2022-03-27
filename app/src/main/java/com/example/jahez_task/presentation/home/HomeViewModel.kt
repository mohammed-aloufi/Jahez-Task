package com.example.jahez_task.presentation.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jahez_task.domain.models.Resource
import com.example.jahez_task.domain.models.RestaurantsState
import com.example.jahez_task.domain.usecase.GetAllRestaurantsUseCase
import com.example.jahez_task.domain.usecase.IsLoggedInUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getAllRestaurantsUseCase: GetAllRestaurantsUseCase,
    private val isLoggedInUseCase: IsLoggedInUseCase
): ViewModel() {


    private val _restaurantsState: MutableSharedFlow<RestaurantsState> = MutableSharedFlow()
    val restaurantsState: SharedFlow<RestaurantsState> = _restaurantsState

    private val _loggedState: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val loggedState: SharedFlow<Boolean> = _loggedState

    init {
        getAllRestaurants()
        isLogged()
    }

    private fun isLogged(){
        viewModelScope.launch {
            isLoggedInUseCase().onEach {
                _loggedState.value = it
                Log.d("view", "$it")
            }.launchIn(viewModelScope)
        }
    }

    private fun getAllRestaurants(){
        viewModelScope.launch {
            getAllRestaurantsUseCase().onEach { result ->
                when(result){
                    is Resource.Loading -> {
                        _restaurantsState.emit(RestaurantsState(isLoading = true))
                    }
                    is Resource.Success -> {
                        _restaurantsState.emit(RestaurantsState(restaurants = result.data ?: emptyList()))
                    }
                    is Resource.Error -> {
                        _restaurantsState.emit(RestaurantsState(message = result.message ?: ""))
                    }
                }
            }.launchIn(viewModelScope)
        }
    }
}