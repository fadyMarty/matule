package com.fadymarty.matule.presentation.profile

sealed interface ProfileEvent {
    data object ToggleNotificationsEnabled : ProfileEvent
    data object Logout : ProfileEvent
    data object NavigateToLogin : ProfileEvent
    data object ShowErrorSnackBar : ProfileEvent
}