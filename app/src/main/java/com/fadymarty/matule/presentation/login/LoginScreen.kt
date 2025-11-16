package com.fadymarty.matule.presentation.login

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.fadymarty.matule.presentation.components.LoadingContent
import com.fadymarty.matule.presentation.navigation.Route
import com.fadymarty.matule_ui_kit.common.theme.MatuleTheme
import com.fadymarty.matule_ui_kit.presentation.components.buttons.BigButton
import com.fadymarty.matule_ui_kit.presentation.components.buttons.LoginButton
import com.fadymarty.matule_ui_kit.presentation.components.icons.MatuleIcons
import com.fadymarty.matule_ui_kit.presentation.components.input.Input
import com.fadymarty.matule_ui_kit.presentation.components.input.PasswordInput
import com.fadymarty.matule_ui_kit.presentation.components.snack_bar.SnackBar
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@Composable
fun LoginScreen(
    navController: NavHostController,
    viewModel: LoginViewModel = koinViewModel(),
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.event.collect { event ->
            when (event) {
                is LoginEvent.NavigateToCreatePin -> {
                    navController.navigate(Route.CreatePinScreen) {
                        popUpTo(Route.LoginScreen) {
                            inclusive = true
                        }
                    }
                }

                is LoginEvent.ShowSnackBar -> {
                    val job = launch {
                        snackbarHostState.showSnackbar("")
                    }
                    delay(5000)
                    job.cancel()
                }

                else -> {}
            }
        }
    }

    AuthContent(
        snackbarHostState = snackbarHostState,
        navController = navController,
        state = state,
        onEvent = viewModel::onEvent
    )
}

@Composable
private fun AuthContent(
    snackbarHostState: SnackbarHostState,
    navController: NavHostController,
    state: LoginState,
    onEvent: (LoginEvent) -> Unit,
) {
    val isButtonActive by remember(state) {
        derivedStateOf {
            state.email.isNotBlank() && state.password.isNotBlank()
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        snackbarHost = {
            SnackbarHost(
                hostState = snackbarHostState
            ) {
                SnackBar(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 20.dp, end = 8.dp),
                    onClose = {
                        snackbarHostState.currentSnackbarData?.dismiss()
                    }
                )
            }
        }
    ) { innerPadding ->
        if (state.isLoading) {
            LoadingContent()
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(
                        start = 20.dp,
                        top = innerPadding.calculateTopPadding(),
                        end = 20.dp,
                        bottom = innerPadding.calculateBottomPadding()
                    )
            ) {
                Spacer(Modifier.height(59.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "✋",
                        fontSize = 32.sp
                    )
                    Spacer(Modifier.width(16.dp))
                    Text(
                        text = "Добро пожаловать!",
                        style = MatuleTheme.typography.title1Semibold
                    )
                }
                Spacer(Modifier.height(23.dp))
                Text(
                    text = "Войдите, чтобы пользоваться функциями приложения",
                    style = MatuleTheme.typography.textRegular
                )
                Spacer(Modifier.height(64.dp))
                Input(
                    value = state.email,
                    onValueChange = {
                        onEvent(LoginEvent.EmailChanged(it))
                    },
                    label = "Вход по E-mail",
                    hint = "example@mail.com",
                    error = if (!state.isEmailValid) {
                        "Введите корректный email"
                    } else null
                )
                Spacer(Modifier.height(14.dp))
                PasswordInput(
                    value = state.password,
                    onValueChange = {
                        onEvent(LoginEvent.PasswordChanged(it))
                    },
                    label = "Пароль",
                    error = if (!state.isPasswordValid) {
                        "Пароль должен содержать заглавные и строчные буквы, цифры, пробелы и специальные символы и быть не менее 8 символов"
                    } else null
                )
                Spacer(Modifier.height(14.dp))
                BigButton(
                    label = "Далее",
                    onClick = {
                        onEvent(LoginEvent.Login)
                    },
                    active = isButtonActive
                )
                Spacer(Modifier.height(15.dp))
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable(
                            interactionSource = null,
                            indication = null
                        ) {
                            navController.navigate(Route.RegisterScreen)
                        },
                    text = "Зарегистрироваться",
                    style = MatuleTheme.typography.textRegular,
                    color = MatuleTheme.colorScheme.accent,
                    textAlign = TextAlign.Center
                )
                Spacer(Modifier.weight(1f))
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = "Или войдите с помощью",
                    style = MatuleTheme.typography.textRegular,
                    color = MatuleTheme.colorScheme.placeholder,
                    textAlign = TextAlign.Center
                )
                Spacer(Modifier.height(16.dp))
                LoginButton(
                    label = "Войти с VK",
                    onClick = {},
                    trailingIcon = MatuleIcons.Vk
                )
                Spacer(Modifier.height(16.dp))
                LoginButton(
                    label = "Войти с Yandex",
                    onClick = {},
                    trailingIcon = MatuleIcons.Yandex
                )
                Spacer(Modifier.height(56.dp))
            }
        }
    }
}