package com.example.jahez_task.presentation.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jahez_task.base.BaseViewModel
import com.example.jahez_task.domain.models.Resource
import com.example.jahez_task.domain.models.Restaurant
import com.example.jahez_task.domain.models.RestaurantsState
import com.example.jahez_task.domain.usecase.GetAllRestaurantsUseCase
import com.example.jahez_task.domain.usecase.IsLoggedInUseCase
import com.example.jahez_task.utils.Constants.AUTO_SORT
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getAllRestaurantsUseCase: GetAllRestaurantsUseCase,
    private val isLoggedInUseCase: IsLoggedInUseCase
): BaseViewModel() {


    private val _restaurants: MutableStateFlow<List<Restaurant>> = MutableStateFlow(emptyList())
    val restaurants: StateFlow<List<Restaurant>> = _restaurants

    private val _loggedState: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val loggedState: SharedFlow<Boolean> = _loggedState

    var sortBy = AUTO_SORT

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
            collectFlow(getAllRestaurantsUseCase()){ result ->
                _restaurants.value = result.data ?: emptyList()
            }
        }
    }
}