package com.fadymarty.matule.presentation.authentication.sign_in

sealed class SignInEvent {
    data class EmailChanged(val email: String) : SignInEvent()
    data class PasswordChanged(val password: String) : SignInEvent()
    data class ToggleVisualTransformation(val isPasswordShown: Boolean) : SignInEvent()
    object SignIn: SignInEvent()
}