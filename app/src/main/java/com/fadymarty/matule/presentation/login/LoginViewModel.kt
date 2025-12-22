package com.fadymarty.matule.presentation.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fadymarty.matule.domain.use_case.validation.ValidateEmailUseCase
import com.fadymarty.matule.domain.use_case.validation.ValidatePasswordUseCase
import com.fadymarty.network.domain.use_case.user.LoginUseCase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LoginViewModel(
    private val validateEmailUseCase: ValidateEmailUseCase,
    private val validatePasswordUseCase: ValidatePasswordUseCase,
    private val loginUseCase: LoginUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow(LoginState())
    val state = _state.asStateFlow()

    private val _event: Channel<LoginEvent> = Channel()
    val event = _event.receiveAsFlow()

    fun onEvent(event: LoginEvent) {
        when (event) {
            is LoginEvent.EmailChanged -> {
                _state.update { it.copy(email = event.value) }
            }

            is LoginEvent.PasswordChanged -> {
                _state.update { it.copy(password = event.value) }
            }

            is LoginEvent.Login -> {
                login()
            }

            else -> Unit
        }
    }

    private fun login() {
        val isEmailValid = validateEmailUseCase(_state.value.email)
        val isPasswordValid = validatePasswordUseCase(_state.value.password)
        _state.update {
            it.copy(
                isEmailValid = isEmailValid,
                isPasswordValid = isPasswordValid
            )
        }
        if (!isEmailValid || !isPasswordValid) {
            return
        }

        _state.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            loginUseCase(
                email = _state.value.email,
                password = _state.value.password
            )
                .onSuccess {
                    _event.send(LoginEvent.NavigateToCreatePin)
                }
                .onFailure {
                    _state.update { it.copy(isLoading = false) }
                    _event.send(LoginEvent.ShowErrorSnackBar)
                }
        }
    }
}