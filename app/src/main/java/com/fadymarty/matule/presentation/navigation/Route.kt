package com.fadymarty.matule.presentation.navigation

import kotlinx.serialization.Serializable

sealed interface Route {

    @Serializable
    data object SplashScreen : Route

    @Serializable
    data object LoginScreen : Route

    @Serializable
    data object RegisterScreen : Route

    @Serializable
    data object CreatePasswordScreen : Route

    @Serializable
    data object CreatePinScreen : Route

    @Serializable
    data object EnterPinScreen : Route

    @Serializable
    data object MainNavigation : Route

    @Serializable
    data object MainScreen : Route

    @Serializable
    data object HomeScreen : Route

    @Serializable
    data object CatalogScreen : Route

    @Serializable
    data object CartScreen : Route

    @Serializable
    data object ProjectsNavigation : Route

    @Serializable
    data object ProjectsScreen : Route

    @Serializable
    data object CreateProjectScreen : Route

    @Serializable
    data object ProfileScreen : Route
}