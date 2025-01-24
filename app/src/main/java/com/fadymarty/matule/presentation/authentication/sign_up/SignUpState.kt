package com.fadymarty.matule.presentation.authentication.sign_up

data class SignUpState(
    val name: String = "",
    val nameError: String? = null,
    val email: String = "",
    val emailError: String? = null,
    val password: String = "",
    val passwordError: String? = null,
    val isPasswordShown: Boolean = false,
    val acceptedTerms: Boolean = false,
    val termsError: String? = null
)