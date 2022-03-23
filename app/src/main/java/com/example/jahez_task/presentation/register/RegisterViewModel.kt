package com.example.jahez_task.presentation.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jahez_task.domain.models.AuthState
import com.example.jahez_task.domain.usecase.RegisterUseCase
import com.example.jahez_task.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val registerUseCase: RegisterUseCase
): ViewModel() {

    private val _registerState: MutableSharedFlow<AuthState> = MutableSharedFlow()
    val registerState: SharedFlow<AuthState> = _registerState

    var isPasswordVisible = false

    fun register(name: String, email: String, password: String){
        viewModelScope.launch {
            registerUseCase(name, email, password).onEach { result ->
                when(result){
                    is Resource.Loading -> {
                        _registerState.emit(AuthState(isLoading = true))
                    }
                    is Resource.Success -> {
                        _registerState.emit(AuthState(isSuccessful = result.data!!))
                    }
                    is Resource.Error -> {
                        _registerState.emit(AuthState(message = result.message ?: "Unknown error!"))
                    }
                }
            }.launchIn(viewModelScope)
        }
    }
}