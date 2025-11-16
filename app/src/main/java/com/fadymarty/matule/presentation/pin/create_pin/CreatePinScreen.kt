package com.fadymarty.matule.presentation.pin.create_pin

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.navigation.NavHostController
import com.fadymarty.matule.presentation.navigation.Route
import com.fadymarty.matule.presentation.pin.components.PinContent
import org.koin.androidx.compose.koinViewModel

@Composable
fun CreatePinScreen(
    navController: NavHostController,
    viewModel: CreatePinViewModel = koinViewModel(),
) {
    CreatePinContent(
        navController = navController,
        onEvent = viewModel::onEvent,
    )
}

@Composable
private fun CreatePinContent(
    navController: NavHostController,
    onEvent: (CreatePinEvent) -> Unit,
) {
    var pin by rememberSaveable {
        mutableStateOf("")
    }
    var selectedNumber by rememberSaveable {
        mutableStateOf<Int?>(null)
    }

    LaunchedEffect(pin) {
        if (pin.length == 4) {
            onEvent(CreatePinEvent.SavePin(pin))
            navController.navigate(Route.MainNavigation) {
                popUpTo(Route.CreatePinScreen) {
                    inclusive = true
                }
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
        title = "Создайте пароль",
        subtitle = "Для защиты ваших персональных данных"
    )
}