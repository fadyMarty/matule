package com.fadymarty.matule.presentation.pin.enter_pin

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.fadymarty.matule.presentation.navigation.Route
import com.fadymarty.matule.presentation.pin.components.PinContent
import org.koin.androidx.compose.koinViewModel

@Composable
fun EnterPinScreen(
    navController: NavHostController,
    viewModel: EnterPinViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    EnterPinContent(
        navController = navController,
        state = state
    )
}

@Composable
private fun EnterPinContent(
    navController: NavHostController,
    state: EnterPinState,
) {
    var pin by rememberSaveable {
        mutableStateOf("")
    }
    var selectedNumber by rememberSaveable {
        mutableStateOf<Int?>(null)
    }

    LaunchedEffect(pin) {
        if (pin.length == 4) {
            if (pin == state.pin) {
                navController.navigate(Route.MainNavigation) {
                    popUpTo(Route.EnterPinScreen) {
                        inclusive = true
                    }
                }
            } else {
                pin = ""
                selectedNumber = null
            }
        }
    }

    PinContent(
        pin = pin,
        onNumberClick = { number ->
            if (pin.length < 4) {
                pin += number
                selectedNumber = number
            }
        },
        onDeleteClick = {
            pin = pin.dropLast(1)
            selectedNumber = null
        },
        selectedNumber = selectedNumber,
        title = "Вход"
    )
}