package com.fadymarty.matule.presentation.profile

sealed class ProfileEvent {
    object ToggleNotificationsEnabled : ProfileEvent()
    object Logout : ProfileEvent()
    object NavigateToLogin : ProfileEvent()
    object ShowSnackBar : ProfileEvent()
}