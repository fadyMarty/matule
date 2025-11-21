package com.fadymarty.matule.presentation.cart

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.fadymarty.matule.R
import com.fadymarty.matule.presentation.components.LoadingContent
import com.fadymarty.matule.presentation.navigation.Route
import com.fadymarty.matule_ui_kit.common.theme.MatuleTheme
import com.fadymarty.matule_ui_kit.presentation.components.buttons.BigButton
import com.fadymarty.matule_ui_kit.presentation.components.cards.CartCard
import com.fadymarty.matule_ui_kit.presentation.components.header.BigHeader
import com.fadymarty.matule_ui_kit.presentation.components.snack_bar.SnackBar
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@Composable
fun CartScreen(
    rootNavController: NavHostController,
    viewModel: CartViewModel = koinViewModel(),
) {
    val context = LocalContext.current
    val snackBarHostState = remember { SnackbarHostState() }

    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.event.collect { event ->
            when (event) {
                is CartEvent.ShowErrorSnackBar -> {
                    val job = launch {
                        snackBarHostState.showSnackbar(
                            message = context.getString(R.string.error_message),
                            duration = SnackbarDuration.Indefinite
                        )
                    }
                    delay(5000)
                    job.cancel()
                }

                is CartEvent.ShowSuccessSnackBar -> {
                    val job = launch {
                        snackBarHostState.showSnackbar(
                            message = "Заказ успешно создан",
                            duration = SnackbarDuration.Indefinite
                        )
                    }
                    delay(5000)
                    job.cancel()
                    rootNavController.navigate(Route.MainNavigation) {
                        popUpTo(Route.CartScreen) {
                            inclusive = true
                        }
                    }
                }

                else -> {}
            }
        }
    }

    CartContent(
        state = state,
        onEvent = viewModel::onEvent,
        navController = rootNavController,
        snackbarHostState = snackBarHostState
    )
}

@Composable
private fun CartContent(
    state: CartState,
    onEvent: (CartEvent) -> Unit,
    navController: NavHostController,
    snackbarHostState: SnackbarHostState,
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            BigHeader(
                modifier = Modifier
                    .statusBarsPadding()
                    .padding(
                        start = 20.dp,
                        top = 16.dp,
                        end = 26.dp,
                        bottom = 8.dp
                    ),
                label = "Корзина",
                onNavigateBack = {
                    navController.navigateUp()
                },
                onDeleteClick = {
                    onEvent(CartEvent.ClearBucket)
                }
            )
        },
        snackbarHost = {
            SnackbarHost(
                hostState = snackbarHostState,
                snackbar = {
                    SnackBar(
                        modifier = Modifier.padding(start = 20.dp, end = 8.dp),
                        message = it.visuals.message,
                        onClose = {
                            snackbarHostState.currentSnackbarData?.dismiss()
                        }
                    )
                }
            )
        }
    ) { innerPadding ->
        if (state.isLoading) {
            LoadingContent(
                modifier = Modifier.padding(top = 24.dp)
            )
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(
                        top = innerPadding.calculateTopPadding(),
                        bottom = innerPadding.calculateBottomPadding()
                    )
            ) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentPadding = PaddingValues(
                        start = 20.dp,
                        top = 24.dp,
                        end = 20.dp,
                        bottom = 32.dp
                    )
                ) {
                    items(state.bucket) { cart ->
                        val product = state.catalog.first { it.id == cart.productId }

                        CartCard(
                            title = product.title,
                            price = product.price,
                            onMinusClick = {
                                onEvent(CartEvent.CountChanged(cart.copy(count = cart.count - 1)))
                            },
                            onPlusClick = {
                                onEvent(CartEvent.CountChanged(cart.copy(count = cart.count + 1)))
                            },
                            onDeleteClick = {
                                onEvent(CartEvent.DeleteCart(cart))
                            },
                            count = cart.count
                        )
                        Spacer(Modifier.height(32.dp))
                    }

                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "Сумма",
                                style = MatuleTheme.typography.title2SemiBold
                            )
                            Spacer(Modifier.weight(1f))
                            Text(
                                text = "${
                                    state.bucket.sumOf { cart ->
                                        state.catalog.first {
                                            it.id == cart.productId
                                        }.price * cart.count
                                    }
                                } ₽",
                                style = MatuleTheme.typography.title2SemiBold
                            )
                            Spacer(Modifier.width(3.dp))
                        }
                    }
                }
                BigButton(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            horizontal = 20.dp,
                            vertical = 32.dp
                        ),
                    label = "Перейти к оформлению заказа",
                    onClick = {
                        onEvent(CartEvent.CreateOrder)
                    },
                    active = state.bucket.isNotEmpty()
                )
            }
        }
    }
}