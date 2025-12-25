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
import com.fadymarty.matule.presentation.pin.components.PinScreen
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun EnterPinRoot(
    navController: NavHostController,
    viewModel: EnterPinViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    EnterPinScreen(
        navController = navController,
        state = state
    )
}

@Composable
private fun EnterPinScreen(
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
                navController.navigate(Route.MainGraph) {
                    popUpTo(Route.EnterPin) {
                        inclusive = true
                    }
                }
            } else {
                pin = ""
                selectedNumber = null
            }
        }
    }

    PinScreen(
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