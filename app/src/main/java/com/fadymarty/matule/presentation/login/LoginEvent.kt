package com.fadymarty.matule.presentation.login

sealed class LoginEvent {
    data class EmailChanged(val value: String) : LoginEvent()
    data class PasswordChanged(val value: String) : LoginEvent()
    object Login : LoginEvent()
    object NavigateToCreatePin : LoginEvent()
    object ShowErrorSnackBar : LoginEvent()
}