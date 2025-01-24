package com.fadymarty.matule.presentation.authentication.sign_up

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fadymarty.matule.domain.repository.UserRepository
import com.fadymarty.matule.domain.use_case.authentication.ValidateEmail
import com.fadymarty.matule.domain.use_case.authentication.ValidateName
import com.fadymarty.matule.domain.use_case.authentication.ValidatePassword
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val validateName: ValidateName,
    private val validateEmail: ValidateEmail,
    private val validatePassword: ValidatePassword,
    private val userRepository: UserRepository
) : ViewModel() {

    private val _state = MutableStateFlow(SignUpState())
    val state = _state.asStateFlow()

    private val _isDialogShown = MutableStateFlow(false)
    val isDialogShown = _isDialogShown.asStateFlow()

    private val validationEventChannel = Channel<ValidationEvent>()
    val validationEvents = validationEventChannel.receiveAsFlow()

    fun onEvent(event: SignUpEvent) {
        when (event) {
            is SignUpEvent.NameChanged -> {
                _state.value = _state.value.copy(name = event.name)
            }

            is SignUpEvent.EmailChanged -> {
                _state.value = _state.value.copy(email = event.email)
            }

            is SignUpEvent.PasswordChanged -> {
                _state.value = _state.value.copy(password = event.password)
            }

            is SignUpEvent.ToggleVisualTransformation -> {
                _state.value = _state.value.copy(
                    isPasswordShown = !event.isPasswordShown
                )
            }

            is SignUpEvent.SignUp -> {
                signUp()
            }
        }
    }

    private fun signUp() {
        val nameResult = validateName(_state.value.name)
        val emailResult = validateEmail(_state.value.email)
        val passwordResult = validatePassword(state.value.password)

        val hasError = listOf(
            nameResult,
            emailResult,
            passwordResult
        ).any { !it.successful }

        if (hasError) {
            _state.value = _state.value.copy(
                nameError = nameResult.errorMessage,
                emailError = emailResult.errorMessage,
                passwordError = passwordResult.errorMessage
            )
            _isDialogShown.value = true
            return
        }

        viewModelScope.launch {
            val result = userRepository.signUp(
                _state.value.name,
                _state.value.email,
                _state.value.password
            )

            if (!result) {
                _isDialogShown.value = true
            } else {
                _isDialogShown.value = false
                validationEventChannel.send(ValidationEvent.Success)
            }
        }
    }

    fun onDismissDialog() {
        _isDialogShown.value = false
    }

    sealed class ValidationEvent {
        object Success: ValidationEvent()
    }
}