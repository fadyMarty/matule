package com.fadymarty.matule.presentation.login

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.fadymarty.matule.R
import com.fadymarty.matule.presentation.components.LoadingScreen
import com.fadymarty.matule_ui_kit.common.theme.MatuleTheme
import com.fadymarty.matule_ui_kit.presentation.components.buttons.BigButton
import com.fadymarty.matule_ui_kit.presentation.components.buttons.LoginButton
import com.fadymarty.matule_ui_kit.presentation.components.input.Input
import com.fadymarty.matule_ui_kit.presentation.components.input.PasswordInput
import com.fadymarty.matule_ui_kit.presentation.components.snack_bar.SnackBar
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel

@SuppressLint("LocalContextGetResourceValueCall")
@Composable
fun LoginRoot(
    onNavigateToRegister: () -> Unit,
    onNavigateToCreatePin: () -> Unit,
    viewModel: LoginViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                LoginEvent.NavigateToRegister -> onNavigateToRegister()
                LoginEvent.NavigateToCreatePin -> onNavigateToCreatePin()
                is LoginEvent.ShowErrorSnackBar -> {
                    val job = launch {
                        snackbarHostState.showSnackbar(
                            message = context.getString(R.string.error_message),
                            duration = SnackbarDuration.Indefinite
                        )
                    }
                    delay(5000)
                    job.cancel()
                }

                else -> Unit
            }
        }
    }

    LoginScreen(
        snackbarHostState = snackbarHostState,
        state = state,
        onEvent = viewModel::onEvent
    )
}

@Composable
private fun LoginScreen(
    state: LoginState,
    onEvent: (LoginEvent) -> Unit,
    snackbarHostState: SnackbarHostState,
) {
    var isPasswordVisible by rememberSaveable {
        mutableStateOf(false)
    }

    Scaffold(
        snackbarHost = {
            SnackbarHost(
                hostState = snackbarHostState
            ) {
                SnackBar(
                    modifier = Modifier.padding(start = 20.dp, end = 8.dp),
                    message = it.visuals.message,
                    onDismiss = {
                        it.dismiss()
                    },
                )
            }
        }
    ) { innerPadding ->
        if (state.isLoading) {
            LoadingScreen(
                modifier = Modifier.padding(innerPadding)
            )
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(horizontal = 20.dp)
            ) {
                Spacer(Modifier.height(59.dp))
                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "✋",
                        fontSize = 32.sp
                    )
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
                    isVisible = isPasswordVisible,
                    onTrailingIconClick = {
                        isPasswordVisible = !isPasswordVisible
                    }
                )
                Spacer(Modifier.height(14.dp))
                BigButton(
                    label = "Далее",
                    onClick = {
                        onEvent(LoginEvent.Login)
                    },
                    enabled = state.email.isNotBlank() && state.password.isNotBlank()
                )
                Spacer(Modifier.height(15.dp))
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable(
                            interactionSource = null,
                            indication = null
                        ) {
                            onEvent(LoginEvent.NavigateToRegister)
                        },
                    text = "Зарегистрироваться",
                    style = MatuleTheme.typography.textRegular,
                    color = MatuleTheme.colorScheme.accent,
                    textAlign = TextAlign.Center
                )
                Spacer(Modifier.weight(1f))
                Column(
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = "Или войдите с помощью",
                        style = MatuleTheme.typography.textRegular,
                        color = MatuleTheme.colorScheme.placeholder,
                        textAlign = TextAlign.Center
                    )
                    LoginButton(
                        label = "Войти с VK",
                        icon = R.drawable.ic_vk,
                        onClick = {}
                    )
                    LoginButton(
                        label = "Войти с Yandex",
                        icon = R.drawable.ic_yandex,
                        onClick = {}
                    )
                    Spacer(Modifier.height(56.dp))
                }
            }
        }
    }
}