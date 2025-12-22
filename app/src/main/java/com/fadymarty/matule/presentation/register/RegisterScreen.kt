package com.fadymarty.matule.presentation.register

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.fadymarty.matule.presentation.navigation.Route
import com.fadymarty.matule_ui_kit.common.theme.MatuleTheme
import com.fadymarty.matule_ui_kit.presentation.components.buttons.BigButton
import com.fadymarty.matule_ui_kit.presentation.components.input.Input
import com.fadymarty.matule_ui_kit.presentation.components.select.Select
import org.koin.compose.viewmodel.koinActivityViewModel

@Composable
fun RegisterRoot(
    navController: NavHostController,
    viewModel: RegisterViewModel = koinActivityViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current

    LaunchedEffect(context) {
        viewModel.event.collect { event ->
            when (event) {
                is RegisterEvent.NavigateToCreatePassword -> {
                    navController.navigate(Route.CreatePassword)
                }

                else -> Unit
            }
        }
    }

    RegisterScreen(
        state = state,
        onEvent = viewModel::onEvent
    )
}

@Composable
private fun RegisterScreen(
    state: RegisterState,
    onEvent: (RegisterEvent) -> Unit,
) {
    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 20.dp)
        ) {
            Spacer(Modifier.height(32.dp))
            Text(
                text = "Создание Профиля",
                style = MatuleTheme.typography.title1ExtraBold
            )
            Spacer(Modifier.height(44.dp))
            Text(
                text = "Без профиля вы не сможете создавать проекты.",
                style = MatuleTheme.typography.captionRegular,
                color = MatuleTheme.colorScheme.placeholder
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text = "В профиле будут храниться результаты проектов и ваши описания.",
                style = MatuleTheme.typography.captionRegular,
                color = MatuleTheme.colorScheme.placeholder
            )
            Spacer(Modifier.height(32.dp))
            Input(
                value = state.firstName,
                onValueChange = {
                    onEvent(RegisterEvent.FirstNameChanged(it))
                },
                hint = "Имя"
            )
            Spacer(Modifier.height(24.dp))
            Input(
                value = state.secondName,
                onValueChange = {
                    onEvent(RegisterEvent.SecondNameChanged(it))
                },
                hint = "Отчество"
            )
            Spacer(Modifier.height(24.dp))
            Input(
                value = state.lastName,
                onValueChange = {
                    onEvent(RegisterEvent.LastNameChanged(it))
                },
                hint = "Фамилия"
            )
            Spacer(Modifier.height(24.dp))
            Input(
                value = state.dateBirthday,
                onValueChange = {
                    onEvent(RegisterEvent.DateBirthDayChanged(it))
                },
                hint = "Дата рождения"
            )
            Spacer(Modifier.height(24.dp))
            Select(
                items = listOf("Мужской", "Женский"),
                selectedItem = state.gender,
                onItemClick = {
                    onEvent(RegisterEvent.GenderChanged(it))
                },
                hint = "Пол"
            )
            Spacer(Modifier.height(24.dp))
            Input(
                value = state.email,
                onValueChange = {
                    onEvent(RegisterEvent.EmailChanged(it))
                },
                hint = "Почта",
                error = if (!state.isEmailValid) {
                    "Введите корректный email"
                } else null
            )
            Spacer(Modifier.weight(1f))
            BigButton(
                label = "Далее",
                onClick = {
                    onEvent(RegisterEvent.Submit)
                },
                active = state.firstName.isNotBlank() &&
                        state.secondName.isNotBlank() &&
                        state.lastName.isNotBlank() &&
                        state.dateBirthday.isNotBlank() &&
                        state.gender != null &&
                        state.email.isNotBlank()
            )
            Spacer(Modifier.height(32.dp))
        }
    }
}