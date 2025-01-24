package com.fadymarty.matule.presentation.authentication.sign_in

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fadymarty.matule.domain.repository.UserRepository
import com.fadymarty.matule.domain.use_case.authentication.ValidateEmail
import com.fadymarty.matule.domain.use_case.authentication.ValidatePassword
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.sin

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val validateEmail: ValidateEmail,
    private val validatePassword: ValidatePassword,
    private val userRepository: UserRepository
) : ViewModel() {

    private val _state = MutableStateFlow(SignInState())
    val state = _state.asStateFlow()

    private val _isDialogShown = MutableStateFlow(false)
    val isDialogShown = _isDialogShown.asStateFlow()

    private val validationEventChannel = Channel<ValidationEvent>()
    val validationEvents = validationEventChannel.receiveAsFlow()

    fun onEvent(event: SignInEvent) {
        when (event) {
            is SignInEvent.EmailChanged -> {
                _state.value = _state.value.copy(email = event.email)
            }

            is SignInEvent.PasswordChanged -> {
                _state.value = _state.value.copy(password = event.password)
            }

            is SignInEvent.ToggleVisualTransformation -> {
                _state.value = _state.value.copy(
                    isPasswordShown = !event.isPasswordShown
                )
            }

            is SignInEvent.SignIn -> {
                signIn()
            }
        }
    }

    private fun signIn() {
        val emailResult = validateEmail(_state.value.email)
        val passwordResult = validatePassword(state.value.password)

        val hasError = listOf(
            emailResult,
            passwordResult
        ).any { !it.successful }

        if (hasError) {
            _state.value = _state.value.copy(
                emailError = emailResult.errorMessage,
                passwordError = passwordResult.errorMessage
            )
            _isDialogShown.value = true
            return
        }

        viewModelScope.launch {
            val result = userRepository.signIn(
                email = _state.value.email,
                password = _state.value.password
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
        object Success : ValidationEvent()
    }
}