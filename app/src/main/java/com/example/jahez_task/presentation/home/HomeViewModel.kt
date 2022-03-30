package com.example.jahez_task.presentation.home

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.example.jahez_task.base.BaseViewModel
import com.example.jahez_task.domain.models.Restaurant
import com.example.jahez_task.domain.usecase.GetAllRestaurantsUseCase
import com.example.jahez_task.domain.usecase.IsLoggedInUseCase
import com.example.jahez_task.utils.Constants.AUTO_SORT
import com.example.jahez_task.utils.DefaultDispatchers
import com.example.jahez_task.utils.DispatcherProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getAllRestaurantsUseCase: GetAllRestaurantsUseCase,
    private val isLoggedInUseCase: IsLoggedInUseCase,
    private val defaultDispatchers: DispatcherProvider
): BaseViewModel() {


    private val _restaurants: MutableStateFlow<List<Restaurant>> = MutableStateFlow(emptyList())
    val restaurants: StateFlow<List<Restaurant>> = _restaurants

    private val _loggedState: MutableSharedFlow<Boolean> = MutableSharedFlow()
    val loggedState: SharedFlow<Boolean> = _loggedState

    var sortBy = AUTO_SORT

    fun isLogged(){
        viewModelScope.launch(defaultDispatchers.main) {
            isLoggedInUseCase().onEach {
                _loggedState.emit(it)
            }.flowOn(defaultDispatchers.main)
                .launchIn(viewModelScope)
        }
    }

    fun getAllRestaurants(){
        viewModelScope.launch(defaultDispatchers.main) {
            collectFlow(getAllRestaurantsUseCase()){ result ->
                _restaurants.value = result.data ?: emptyList()
            }
        }
    }
}