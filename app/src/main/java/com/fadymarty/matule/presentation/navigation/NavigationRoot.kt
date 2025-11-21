package com.fadymarty.matule.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.fadymarty.matule.presentation.cart.CartScreen
import com.fadymarty.matule.presentation.login.LoginScreen
import com.fadymarty.matule.presentation.main.MainScreen
import com.fadymarty.matule.presentation.pin.create_pin.CreatePinScreen
import com.fadymarty.matule.presentation.pin.enter_pin.EnterPinScreen
import com.fadymarty.matule.presentation.register.CreatePasswordScreen
import com.fadymarty.matule.presentation.register.RegisterScreen
import com.fadymarty.matule.presentation.splash.SplashScreen

@Composable
fun NavGraph() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Route.SplashScreen
    ) {
        composable<Route.SplashScreen> {
            SplashScreen(
                navController = navController
            )
        }

        composable<Route.LoginScreen> {
            LoginScreen(
                navController = navController
            )
        }

        composable<Route.RegisterScreen> {
            RegisterScreen(
                navController = navController
            )
        }

        composable<Route.CreatePasswordScreen> {
            CreatePasswordScreen(
                navController = navController
            )
        }

        composable<Route.CreatePinScreen> {
            CreatePinScreen(
                navController = navController
            )
        }

        composable<Route.EnterPinScreen> {
            EnterPinScreen(
                navController = navController
            )
        }

        navigation<Route.MainNavigation>(
            startDestination = Route.MainScreen
        ) {
            composable<Route.MainScreen> {
                MainScreen(
                    rootNavController = navController
                )
            }
        }

        composable<Route.CartScreen> {
            CartScreen(
                rootNavController = navController
            )
        }
    }
}