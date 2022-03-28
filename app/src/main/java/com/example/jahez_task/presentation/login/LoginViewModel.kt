package com.example.jahez_task.presentation.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jahez_task.base.BaseViewModel
import com.example.jahez_task.domain.models.AuthState
import com.example.jahez_task.domain.usecase.LoginUseCase
import com.example.jahez_task.domain.models.Resource
import com.example.jahez_task.utils.Constants.INVALID_EMAIL
import com.example.jahez_task.utils.Constants.INVALID_PASSWORD
import com.example.jahez_task.utils.Constants.VALID_INPUT
import com.example.jahez_task.utils.InputValidator.isValidEmail
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase
) : BaseViewModel() {

    private val _loginState: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val loginState: StateFlow<Boolean> = _loginState

    private val _inputState: MutableSharedFlow<Int> = MutableSharedFlow()
    val inputState: SharedFlow<Int> = _inputState

    var isPasswordVisible = false

    fun login(email: String, password: String) {
        viewModelScope.launch {
            collectFlow(loginUseCase(email, password)) { result ->
                _loginState.value = result.data ?: false
            }
        }
    }

    fun checkInputs(email: String, password: String) {
        viewModelScope.launch {
            when {
                email.isBlank() && !email.isValidEmail() -> {
                    _inputState.emit(INVALID_EMAIL)
                }
                password.isBlank() -> {
                    _inputState.emit(INVALID_PASSWORD)
                }
                else -> {
                    _inputState.emit(VALID_INPUT)
                }
            }
        }
    }

}