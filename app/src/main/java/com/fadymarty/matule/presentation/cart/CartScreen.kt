package com.fadymarty.matule.presentation.cart

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
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
import com.fadymarty.matule.R
import com.fadymarty.matule.presentation.components.LoadingScreen
import com.fadymarty.matule_ui_kit.common.theme.MatuleTheme
import com.fadymarty.matule_ui_kit.presentation.components.buttons.BackButton
import com.fadymarty.matule_ui_kit.presentation.components.buttons.BigButton
import com.fadymarty.matule_ui_kit.presentation.components.cards.CartCard
import com.fadymarty.matule_ui_kit.presentation.components.header.BigHeader
import com.fadymarty.matule_ui_kit.presentation.components.snack_bar.SnackBar
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel

@SuppressLint("LocalContextGetResourceValueCall")
@Composable
fun CartRoot(
    onNavigateBack: () -> Unit,
    onNavigateToMainGraph: () -> Unit,
    viewModel: CartViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                is CartEvent.NavigateBack -> onNavigateBack()
                is CartEvent.ShowErrorSnackBar -> {
                    val job = launch {
                        snackbarHostState.showSnackbar(
                            message = context.getString(R.string.error_message),
                            duration = SnackbarDuration.Indefinite
                        )
                    }
                    delay(5000)
                    job.cancel()
                }

                is CartEvent.ShowSuccessSnackBar -> {
                    val job = launch {
                        snackbarHostState.showSnackbar(
                            message = "Заказ успешно создан",
                            duration = SnackbarDuration.Indefinite
                        )
                    }
                    delay(5000)
                    job.cancel()
                    onNavigateToMainGraph()
                }

                else -> Unit
            }
        }
    }

    CartScreen(
        state = state,
        onEvent = viewModel::onEvent,
        snackbarHostState = snackbarHostState
    )
}

@Composable
private fun CartScreen(
    state: CartState,
    onEvent: (CartEvent) -> Unit,
    snackbarHostState: SnackbarHostState,
) {
    val price = state.carts.sumOf { cart ->
        state.products.firstOrNull {
            it.id == cart.productId
        }?.let { it.price * cart.count } ?: 0
    }

    Scaffold(
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
                title = "Корзина",
                navigationIcon = {
                    BackButton(
                        onClick = {
                            onEvent(CartEvent.NavigateBack)
                        }
                    )
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
                        onDismiss = {
                            it.dismiss()
                        }
                    )
                }
            )
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
            ) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentPadding = PaddingValues(
                        horizontal = 20.dp,
                        vertical = 32.dp
                    )
                ) {
                    items(state.carts) { cart ->
                        val product = state.products.firstOrNull { it.id == cart.productId }
                        product?.let {
                            CartCard(
                                title = product.title,
                                price = "${product.price} ₽",
                                onMinusClick = {
                                    onEvent(
                                        CartEvent.UpdateCart(
                                            cart.copy(count = cart.count - 1)
                                        )
                                    )
                                },
                                onPlusClick = {
                                    onEvent(
                                        CartEvent.UpdateCart(
                                            cart.copy(count = cart.count + 1)
                                        )
                                    )
                                },
                                onDeleteClick = {
                                    onEvent(CartEvent.DeleteCart(cart))
                                },
                                count = cart.count
                            )
                            Spacer(Modifier.height(32.dp))
                        }
                    }
                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Сумма",
                                style = MatuleTheme.typography.title2SemiBold
                            )
                            Text(
                                modifier = Modifier.padding(end = 3.dp),
                                text = "$price ₽",
                                style = MatuleTheme.typography.title2SemiBold
                            )
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
                    enabled = state.carts.isNotEmpty()
                )
            }
        }
    }
}