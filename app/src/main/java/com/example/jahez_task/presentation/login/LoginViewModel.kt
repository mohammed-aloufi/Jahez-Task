package com.example.jahez_task.presentation.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jahez_task.domain.models.AuthState
import com.example.jahez_task.domain.usecase.LoginUseCase
import com.example.jahez_task.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase
) : ViewModel() {

    private val _loginState: MutableSharedFlow<AuthState> = MutableSharedFlow()
    val loginState: SharedFlow<AuthState> = _loginState

    var isPasswordVisible = false

    fun login(email: String, password: String){
        viewModelScope.launch {
            loginUseCase(email, password).onEach { result ->
                when(result){
                    is Resource.Loading -> {
                        _loginState.emit(AuthState(isLoading = true))
                    }
                    is Resource.Success -> {
                        _loginState.emit(AuthState(isSuccessful = result.data?.isSuccessful ?: false))
                    }
                    is Resource.Error -> {
                        _loginState.emit(AuthState(message = result.message ?: "Unknown error!"))
                    }
                }
            }.launchIn(viewModelScope)
        }
    }
}