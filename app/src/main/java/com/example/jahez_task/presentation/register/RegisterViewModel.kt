package com.example.jahez_task.presentation.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jahez_task.R
import com.example.jahez_task.domain.models.AuthState
import com.example.jahez_task.domain.usecase.RegisterUseCase
import com.example.jahez_task.domain.models.Resource
import com.example.jahez_task.utils.Constants
import com.example.jahez_task.utils.Constants.EMPTY_EMAIL
import com.example.jahez_task.utils.Constants.EMPTY_PASSWORD
import com.example.jahez_task.utils.Constants.INVALID_EMAIL
import com.example.jahez_task.utils.Constants.INVALID_NAME
import com.example.jahez_task.utils.Constants.INVALID_PASSWORD
import com.example.jahez_task.utils.Constants.PASSWORDS_NOT_MATCHING
import com.example.jahez_task.utils.Constants.VALID_INPUT
import com.example.jahez_task.utils.InputValidator.isValidEmail
import com.example.jahez_task.utils.InputValidator.isValidPassword
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val registerUseCase: RegisterUseCase
) : ViewModel() {

    private val _registerState: MutableSharedFlow<AuthState> = MutableSharedFlow()
    val registerState: SharedFlow<AuthState> = _registerState

    private val _inputState: MutableSharedFlow<Int> = MutableSharedFlow()
    val inputState: SharedFlow<Int> = _inputState

    var isPasswordVisible = false

    fun register(name: String, email: String, password: String) {
        viewModelScope.launch {
            registerUseCase(name, email, password).onEach { result ->
                when (result) {
                    is Resource.Loading -> {
                        _registerState.emit(AuthState(isLoading = true))
                    }
                    is Resource.Success -> {
                        _registerState.emit(
                            AuthState(
                                isSuccessful = result.data?.isSuccessful ?: false
                            )
                        )
                    }
                    is Resource.Error -> {
                        _registerState.emit(AuthState(message = result.message ?: "Unknown error!"))
                    }
                }
            }.launchIn(viewModelScope)
        }
    }

    fun isInputValid(name: String, email: String, password: String, confirmPassword: String) {
        viewModelScope.launch {
            when {
                name.isBlank() -> {
                    _inputState.emit(INVALID_NAME)
                }
                email.isBlank() -> {
                    _inputState.emit(EMPTY_EMAIL)
                }
                !email.isValidEmail() -> {
                    _inputState.emit(INVALID_EMAIL)
                }
                password.isBlank() -> {
                    _inputState.emit(EMPTY_PASSWORD)
                }
                password != confirmPassword -> {
                    _inputState.emit(PASSWORDS_NOT_MATCHING)
                }
                !password.isValidPassword() -> {
                    _inputState.emit(INVALID_PASSWORD)
                }
                else -> {
                    _inputState.emit(VALID_INPUT)
                }
            }
        }
    }
}