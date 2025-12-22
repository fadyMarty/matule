package com.fadymarty.matule.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.fadymarty.matule.presentation.cart.CartRoot
import com.fadymarty.matule.presentation.login.LoginRoot
import com.fadymarty.matule.presentation.main.MainScreen
import com.fadymarty.matule.presentation.pin.create_pin.CreatePinRoot
import com.fadymarty.matule.presentation.pin.enter_pin.EnterPinRoot
import com.fadymarty.matule.presentation.register.PasswordRoot
import com.fadymarty.matule.presentation.register.RegisterRoot
import com.fadymarty.matule.presentation.splash.SplashRoot

@Composable
fun NavigationRoot() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Route.Splash
    ) {
        composable<Route.Splash> {
            SplashRoot(
                navController = navController
            )
        }

        composable<Route.Login> {
            LoginRoot(
                navController = navController
            )
        }

        composable<Route.Register> {
            RegisterRoot(
                navController = navController
            )
        }

        composable<Route.CreatePassword> {
            PasswordRoot(
                navController = navController
            )
        }

        composable<Route.CreatePin> {
            CreatePinRoot(
                navController = navController
            )
        }

        composable<Route.EnterPin> {
            EnterPinRoot(
                navController = navController
            )
        }

        navigation<Route.MainGraph>(
            startDestination = Route.Main
        ) {
            composable<Route.Main> {
                MainScreen(
                    rootNavController = navController
                )
            }
        }

        composable<Route.Cart> {
            CartRoot(
                rootNavController = navController
            )
        }
    }
}