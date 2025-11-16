package com.fadymarty.matule.presentation.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fadymarty.matule.domain.use_case.validation.ValidateEmailUseCase
import com.fadymarty.matule.domain.use_case.validation.ValidatePasswordConfirmUseCase
import com.fadymarty.matule.domain.use_case.validation.ValidatePasswordUseCase
import com.fadymarty.network.domain.model.User
import com.fadymarty.network.domain.use_case.user.RegisterUseCase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class RegisterViewModel(
    private val registerUseCase: RegisterUseCase,
    private val validateEmailUseCase: ValidateEmailUseCase,
    private val validatePasswordUseCase: ValidatePasswordUseCase,
    private val validatePasswordConfirmUseCase: ValidatePasswordConfirmUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow(RegisterState())
    val state = _state.asStateFlow()

    private val _event: Channel<RegisterEvent> = Channel()
    val event = _event.receiveAsFlow()

    fun onEvent(event: RegisterEvent) {
        when (event) {
            is RegisterEvent.FirstNameChanged -> {
                _state.update { it.copy(firstName = event.value) }
            }

            is RegisterEvent.SecondNameChanged -> {
                _state.update { it.copy(secondName = event.value) }
            }

            is RegisterEvent.LastNameChanged -> {
                _state.update { it.copy(lastName = event.value) }
            }

            is RegisterEvent.DateBirthDayChanged -> {
                _state.update { it.copy(dateBirthday = event.value) }
            }

            is RegisterEvent.GenderChanged -> {
                _state.update { it.copy(gender = event.value) }
            }

            is RegisterEvent.EmailChanged -> {
                _state.update { it.copy(email = event.value) }
            }

            is RegisterEvent.PasswordChanged -> {
                _state.update { it.copy(password = event.value) }
            }

            is RegisterEvent.PasswordConfirmChanged -> {
                _state.update { it.copy(passwordConfirm = event.value) }
            }

            is RegisterEvent.Submit -> {
                submit()
            }

            is RegisterEvent.Register -> {
                register()
            }

            else -> {}
        }
    }

    private fun submit() {
        val isEmailValid = validateEmailUseCase(_state.value.email)
        _state.update { it.copy(isEmailValid = isEmailValid) }
        if (!isEmailValid) {
            return
        }
        viewModelScope.launch {
            _event.send(RegisterEvent.NavigateToCreatePassword)
        }
    }

    private fun register() {
        val isPasswordValid = validatePasswordUseCase(_state.value.password)
        val isPasswordConfirmValid = validatePasswordConfirmUseCase(
            password = _state.value.password,
            passwordConfirm = _state.value.passwordConfirm
        )
        _state.update {
            it.copy(
                isPasswordValid = isPasswordValid,
                isPasswordConfirmValid = isPasswordConfirmValid
            )
        }
        if (!isPasswordValid || !isPasswordConfirmValid || _state.value.gender == null) {
            return
        }
        val user = User(
            firstName = _state.value.firstName,
            lastName = _state.value.lastName,
            secondName = _state.value.secondName,
            dateBirthday = _state.value.dateBirthday,
            gender = _state.value.gender!!,
            email = _state.value.email,
            password = _state.value.password,
            passwordConfirm = _state.value.passwordConfirm
        )

        _state.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            registerUseCase(user)
                .onSuccess {
                    _event.send(RegisterEvent.NavigateToCreatePin)
                }
                .onFailure {
                    _state.update { it.copy(isLoading = false) }
                    _event.send(RegisterEvent.ShowSnackBar)
                }
        }
    }
}