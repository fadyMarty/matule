package com.fadymarty.matule.presentation.authentication.sign_up

sealed class SignUpEvent {
    data class NameChanged(val name: String) : SignUpEvent()
    data class EmailChanged(val email: String) : SignUpEvent()
    data class PasswordChanged(val password: String) : SignUpEvent()
    data class ToggleVisualTransformation(val isPasswordShown: Boolean) : SignUpEvent()
    object SignUp: SignUpEvent()
}