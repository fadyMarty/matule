package com.fadymarty.matule.presentation.profile

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import com.fadymarty.matule.presentation.components.LoadingContent
import com.fadymarty.matule.presentation.navigation.Route
import com.fadymarty.matule_ui_kit.common.theme.MatuleTheme
import com.fadymarty.matule_ui_kit.presentation.components.controls.Toggle
import com.fadymarty.matule_ui_kit.presentation.components.snack_bar.SnackBar
import com.rajat.pdfviewer.PdfViewerActivity
import com.rajat.pdfviewer.util.saveTo
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@Composable
fun ProfileScreen(
    rootNavController: NavHostController,
    viewModel: ProfileViewModel = koinViewModel(),
) {
    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.event.collect { event ->
            when (event) {
                is ProfileEvent.ShowSnackBar -> {
                    val job = launch {
                        snackbarHostState.showSnackbar(
                            message = context.getString(R.string.error_message)
                        )
                    }
                    delay(5000)
                    job.cancel()
                }

                is ProfileEvent.NavigateToLogin -> {
                    rootNavController.navigate(Route.LoginScreen) {
                        popUpTo(Route.MainScreen) {
                            inclusive = true
                        }
                    }
                }

                else -> {}
            }
        }
    }

    ProfileContent(
        snackbarHostState = snackbarHostState,
        state = state,
        onEvent = viewModel::onEvent
    )
}

@Composable
private fun ProfileContent(
    snackbarHostState: SnackbarHostState,
    state: ProfileState,
    onEvent: (ProfileEvent) -> Unit,
) {
    val context = LocalContext.current

    Scaffold(
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
                    },
                    message = it.visuals.message
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(
                    start = 20.dp,
                    top = innerPadding.calculateTopPadding() + 32.dp,
                    end = 20.dp
                )
        ) {
            if (state.isLoading) {
                LoadingContent()
            } else {
                state.user?.let { user ->
                    Text(
                        text = user.firstName,
                        style = MatuleTheme.typography.title1ExtraBold
                    )
                    Spacer(Modifier.height(8.dp))
                    Text(
                        text = user.email,
                        style = MatuleTheme.typography.headlineRegular,
                        color = MatuleTheme.colorScheme.placeholder
                    )
                    Spacer(Modifier.height(23.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 16.dp)
                            .clickable(
                                interactionSource = null,
                                indication = null
                            ) {

                            },
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "📋",
                            fontSize = 32.sp
                        )
                        Spacer(Modifier.width(20.dp))
                        Text(
                            text = "Мои заказы"
                        )
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "⚙️",
                            fontSize = 32.sp
                        )
                        Spacer(Modifier.width(20.dp))
                        Text(
                            text = "Уведомления"
                        )
                        Spacer(Modifier.weight(1f))
                        Toggle(
                            checked = state.isNotificationsEnabled,
                            onClick = {
                                onEvent(ProfileEvent.ToggleNotificationsEnabled)
                            }
                        )
                    }
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            modifier = Modifier
                                .clickable(
                                    interactionSource = null,
                                    indication = null
                                ) {
                                    context.startActivity(
                                        PdfViewerActivity.launchPdfFromUrl(
                                            context = context,
                                            pdfUrl = "https://www.back4app.com/terms-of-service.pdf",
                                            pdfTitle = null,
                                            saveTo = saveTo.DOWNLOADS
                                        )
                                    )
                                },
                            text = "Политика конфиденциальности",
                            style = MatuleTheme.typography.textMedium,
                            color = MatuleTheme.colorScheme.placeholder
                        )
                        Spacer(Modifier.height(24.dp))
                        Text(
                            modifier = Modifier
                                .clickable(
                                    interactionSource = null,
                                    indication = null
                                ) {
                                    context.startActivity(
                                        PdfViewerActivity.launchPdfFromUrl(
                                            context = context,
                                            pdfUrl = "https://www.back4app.com/terms-of-service.pdf",
                                            pdfTitle = null,
                                            saveTo = saveTo.DOWNLOADS
                                        )
                                    )
                                },
                            text = "Пользовательское соглашение",
                            style = MatuleTheme.typography.textMedium,
                            color = MatuleTheme.colorScheme.placeholder
                        )
                        Spacer(Modifier.height(24.dp))
                        Text(
                            modifier = Modifier
                                .clickable(
                                    interactionSource = null,
                                    indication = null
                                ) {
                                    onEvent(ProfileEvent.Logout)
                                },
                            text = "Выход",
                            style = MatuleTheme.typography.textMedium,
                            color = MatuleTheme.colorScheme.error
                        )
                    }
                }
            }
        }
    }
}