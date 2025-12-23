package com.fadymarty.matule.presentation.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.fadymarty.matule.R
import com.fadymarty.matule.presentation.navigation.Route
import com.fadymarty.matule_ui_kit.common.theme.MatuleTheme
import com.fadymarty.matule_ui_kit.common.theme.RobotoFamily
import org.koin.androidx.compose.koinViewModel

@Composable
fun SplashRoot(
    navController: NavHostController,
    viewModel: SplashViewModel = koinViewModel(),
) {
    val context = LocalContext.current

    LaunchedEffect(context) {
        viewModel.events.collect { event ->
            when (event) {
                SplashEvent.NavigateToEnterPin -> {
                    navController.navigate(Route.EnterPin) {
                        popUpTo(Route.Splash) {
                            inclusive = true
                        }
                    }
                }

                SplashEvent.NavigateToLogin -> {
                    navController.navigate(Route.Login) {
                        popUpTo(Route.Splash) {
                            inclusive = true
                        }
                    }
                }
            }
        }
    }

    SplashScreen()
}

@Composable
private fun SplashScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Image(
            modifier = Modifier.fillMaxSize(),
            painter = painterResource(R.drawable.img_splash_background),
            contentDescription = null,
            contentScale = ContentScale.FillBounds
        )
        Text(
            text = stringResource(R.string.app_name),
            fontFamily = RobotoFamily,
            fontWeight = FontWeight.Normal,
            fontSize = 40.sp,
            lineHeight = 64.sp,
            letterSpacing = 1.04.sp,
            textAlign = TextAlign.Center,
            color = MatuleTheme.colorScheme.onAccent
        )
    }
}