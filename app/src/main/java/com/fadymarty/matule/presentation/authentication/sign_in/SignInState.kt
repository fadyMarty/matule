package com.fadymarty.matule.presentation.authentication.sign_in

data class SignInState(
    val email: String = "",
    val emailError: String? = null,
    val password: String = "",
    val passwordError: String? = null,
    val isPasswordShown: Boolean = false
)