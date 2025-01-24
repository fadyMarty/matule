package com.fadymarty.matule.presentation.navigation.screen

sealed class Screen(val route: String) {
    object Splash : Screen("splash_screen")
    object Onboarding1 : Screen("onboarding_screen_1")
    object Onboarding2 : Screen("onboarding_screen_2")
    object Onboarding3 : Screen("onboarding_screen_3")
    object SignUp: Screen("sign_up_screen")
    object SignIn: Screen("sign_in_screen")
    object Home: Screen("home_screen")
    object Cart: Screen("cart_screen")
}