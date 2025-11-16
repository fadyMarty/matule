package com.fadymarty.matule.presentation.register

sealed class RegisterEvent {
    data class FirstNameChanged(val value: String) : RegisterEvent()
    data class SecondNameChanged(val value: String) : RegisterEvent()
    data class LastNameChanged(val value: String) : RegisterEvent()
    data class DateBirthDayChanged(val value: String) : RegisterEvent()
    data class GenderChanged(val value: String) : RegisterEvent()
    data class EmailChanged(val value: String) : RegisterEvent()
    data class PasswordChanged(val value: String) : RegisterEvent()
    data class PasswordConfirmChanged(val value: String) : RegisterEvent()
    object Submit : RegisterEvent()
    object Register : RegisterEvent()
    object NavigateToCreatePassword : RegisterEvent()
    object NavigateToCreatePin : RegisterEvent()
    object ShowSnackBar : RegisterEvent()
}