package com.fadymarty.matule.presentation.splash

sealed interface SplashEvent {
    object NavigateToEnterPin : SplashEvent
    object NavigateToLogin : SplashEvent
}