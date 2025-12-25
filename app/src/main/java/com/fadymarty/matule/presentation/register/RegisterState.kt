package com.fadymarty.matule.presentation.register

import com.fadymarty.matule_ui_kit.presentation.components.select.SelectItem

data class RegisterState(
    val isLoading: Boolean = false,
    val firstName: String = "",
    val secondName: String = "",
    val lastName: String = "",
    val dateBirthday: String = "",
    val gender: SelectItem? = null,
    val email: String = "",
    val isEmailValid: Boolean = true,
    val password: String = "",
    val isPasswordValid: Boolean = true,
    val passwordConfirm: String = "",
    val isPasswordConfirmValid: Boolean = true,
)
