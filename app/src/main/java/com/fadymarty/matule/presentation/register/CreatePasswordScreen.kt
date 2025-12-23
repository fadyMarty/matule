package com.fadymarty.matule.presentation.register

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.fadymarty.matule.R
import com.fadymarty.matule.presentation.components.LoadingScreen
import com.fadymarty.matule.presentation.navigation.Route
import com.fadymarty.matule_ui_kit.common.theme.MatuleTheme
import com.fadymarty.matule_ui_kit.presentation.components.buttons.BigButton
import com.fadymarty.matule_ui_kit.presentation.components.input.PasswordInput
import com.fadymarty.matule_ui_kit.presentation.components.snack_bar.SnackBar
import org.koin.compose.viewmodel.koinActivityViewModel

@Composable
fun PasswordRoot(
    navController: NavHostController,
    viewModel: RegisterViewModel = koinActivityViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(context) {
        viewModel.events.collect { event ->
            when (event) {
                is RegisterEvent.ShowSnackBar -> {
                    snackbarHostState.showSnackbar(
                        message = context.getString(R.string.error_message)
                    )
                }

                is RegisterEvent.NavigateToCreatePin -> {
                    navController.navigate(Route.CreatePin) {
                        popUpTo(Route.CreatePassword) {
                            inclusive = true
                        }
                    }
                }

                else -> Unit
            }
        }
    }

    PasswordScreen(
        snackbarHostState = snackbarHostState,
        state = state,
        onEvent = viewModel::onEvent
    )
}

@Composable
private fun PasswordScreen(
    state: RegisterState,
    onEvent: (RegisterEvent) -> Unit,
    snackbarHostState: SnackbarHostState,
) {
    Scaffold(
        snackbarHost = {
            SnackbarHost(snackbarHostState) {
                SnackBar(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 20.dp, end = 12.dp),
                    onClose = {
                        snackbarHostState.currentSnackbarData?.dismiss()
                    },
                    message = it.visuals.message
                )
            }
        }
    ) { innerPadding ->
        if (state.isLoading) {
            LoadingScreen()
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
                        text = "Создание пароля",
                        style = MatuleTheme.typography.title1ExtraBold
                    )
                }
                Spacer(Modifier.height(23.dp))
                Text(
                    text = "Введите новый пароль",
                    style = MatuleTheme.typography.textRegular
                )
                Spacer(Modifier.height(90.dp))
                PasswordInput(
                    value = state.password,
                    onValueChange = {
                        onEvent(RegisterEvent.PasswordChanged(it))
                    },
                    label = "Новый Пароль",
                    error = if (!state.isPasswordValid) {
                        "Пароль должен содержать заглавные и строчные буквы, цифры, пробелы и специальные символы и быть не менее 8 символов"
                    } else null
                )
                Spacer(Modifier.height(12.dp))
                PasswordInput(
                    value = state.passwordConfirm,
                    onValueChange = {
                        onEvent(RegisterEvent.PasswordConfirmChanged(it))
                    },
                    label = "Повторите пароль",
                    error = if (!state.isPasswordValid) {
                        "Пароли не совпадают"
                    } else null
                )
                Spacer(Modifier.height(12.dp))
                BigButton(
                    label = "Сохранить",
                    onClick = {
                        onEvent(RegisterEvent.Register)
                    },
                    active = state.password.isNotBlank() && state.passwordConfirm.isNotBlank()
                )
            }
        }
    }
}