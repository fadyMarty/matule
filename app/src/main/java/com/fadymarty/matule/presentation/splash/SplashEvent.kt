package com.fadymarty.matule.presentation.splash

sealed class SplashEvent {
    object NavigateToEnterPin : SplashEvent()
    object NavigateToLogin : SplashEvent()
}