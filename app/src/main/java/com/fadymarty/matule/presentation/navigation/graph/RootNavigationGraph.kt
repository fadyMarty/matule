package com.fadymarty.matule.presentation.navigation.graph

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.fadymarty.matule.presentation.authentication.sign_in.SignInScreen
import com.fadymarty.matule.presentation.authentication.sign_up.SignUpScreen
import com.fadymarty.matule.presentation.navigation.screen.Screen
import com.fadymarty.matule.presentation.onboarding.OnboardingScreen1
import com.fadymarty.matule.presentation.onboarding.OnboardingScreen2
import com.fadymarty.matule.presentation.onboarding.OnboardingScreen3
import com.fadymarty.matule.presentation.splash.SplashScreen

@Composable
fun RootNavigationGraph() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        route = Graph.ROOT,
        startDestination = Screen.Splash.route
    ) {
        composable(Screen.Splash.route) {
            SplashScreen(
                navController = navController
            )
        }

        composable(Screen.Onboarding1.route) {
            OnboardingScreen1(
                navController = navController
            )
        }

        composable(Screen.Onboarding2.route) {
            OnboardingScreen2(
                navController = navController
            )
        }

        composable(Screen.Onboarding3.route) {
            OnboardingScreen3(
                navController = navController
            )
        }

        composable(Screen.SignIn.route) {
            SignInScreen(
                navController = navController
            )
        }

        composable(Screen.SignUp.route) {
            SignUpScreen(
                navController = navController
            )
        }

        composable(Graph.MAIN) {
            MainGraph()
        }
    }
}