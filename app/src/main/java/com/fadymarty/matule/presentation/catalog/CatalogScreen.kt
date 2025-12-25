package com.fadymarty.matule.presentation.catalog

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.fadymarty.matule.R
import com.fadymarty.matule.presentation.components.LoadingScreen
import com.fadymarty.matule.presentation.components.ProductModal
import com.fadymarty.matule_ui_kit.presentation.components.buttons.CartButton
import com.fadymarty.matule_ui_kit.presentation.components.buttons.ChipButton
import com.fadymarty.matule_ui_kit.presentation.components.cards.PrimaryCard
import com.fadymarty.matule_ui_kit.presentation.components.input.SearchInput
import com.fadymarty.matule_ui_kit.presentation.components.snack_bar.SnackBar
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel

@SuppressLint("LocalContextGetResourceValueCall")
@Composable
fun CatalogRoot(
    onNavigateToProfile: () -> Unit,
    onNavigateToCart: () -> Unit,
    viewModel: CatalogViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                is CatalogEvent.NavigateToProfile -> onNavigateToProfile()
                is CatalogEvent.NavigateToCart -> onNavigateToCart()
                is CatalogEvent.ShowErrorSnackBar -> {
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

    CatalogScreen(
        snackbarHostState = snackbarHostState,
        state = state,
        onEvent = viewModel::onEvent
    )
}

@Composable
private fun CatalogScreen(
    state: CatalogState,
    onEvent: (CatalogEvent) -> Unit,
    snackbarHostState: SnackbarHostState,
) {
    Scaffold(
        topBar = {
            Row(
                modifier = Modifier
                    .statusBarsPadding()
                    .padding(top = 28.dp)
                    .padding(horizontal = 20.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(38.dp)
            ) {
                SearchInput(
                    modifier = Modifier.weight(1f),
                    value = state.searchQuery,
                    onValueChange = {
                        onEvent(CatalogEvent.SearchQueryChanged(it))
                    },
                    hint = "Искать описания",
                    onClearClick = {
                        onEvent(CatalogEvent.ClearSearchQuery)
                    }
                )
                Image(
                    modifier = Modifier
                        .size(32.dp)
                        .clickable(
                            interactionSource = null,
                            indication = null,
                            onClick = {
                                onEvent(CatalogEvent.NavigateToProfile)
                            }
                        ),
                    painter = painterResource(R.drawable.img_user_icon),
                    contentDescription = null
                )
            }
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
                modifier = Modifier
                    .padding(
                        top = innerPadding.calculateTopPadding()
                    )
            )
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(
                        top = innerPadding.calculateTopPadding() + 24.dp
                    )
            ) {
                if (state.products.isNotEmpty()) {
                    LazyRow(
                        contentPadding = PaddingValues(horizontal = 20.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        item {
                            ChipButton(
                                selected = state.type == null,
                                label = "Все",
                                onClick = {
                                    onEvent(CatalogEvent.SelectType(null))
                                }
                            )
                        }
                        items(state.types) { type ->
                            ChipButton(
                                selected = type == state.type,
                                label = type,
                                onClick = {
                                    onEvent(CatalogEvent.SelectType(type))
                                }
                            )
                        }
                    }
                    Spacer(Modifier.height(8.dp))
                }
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentPadding = PaddingValues(
                        start = 20.dp,
                        top = 12.dp,
                        end = 20.dp,
                        bottom = 8.dp
                    )
                ) {
                    items(state.products) { product ->
                        PrimaryCard(
                            title = product.title,
                            type = product.type,
                            price = "${product.price} ₽",
                            added = state.carts.any { it.productId == product.id },
                            onClick = {
                                onEvent(CatalogEvent.ShowProductModal(product))
                            },
                            onButtonClick = {
                                onEvent(CatalogEvent.AddProductToCart(product))
                            }
                        )
                        Spacer(Modifier.height(12.dp))
                    }
                }
                if (state.carts.isNotEmpty()) {
                    CartButton(
                        modifier = Modifier
                            .padding(
                                horizontal = 20.dp,
                                vertical = 32.dp
                            ),
                        onClick = {
                            onEvent(CatalogEvent.NavigateToCart)
                        },
                        price = state.carts.sumOf { cart ->
                            state.products.first {
                                it.id == cart.productId
                            }.price * cart.count
                        }
                    )
                }
            }
        }
    }

    state.product?.let { product ->
        ProductModal(
            onDismissRequest = {
                onEvent(CatalogEvent.HideProductModal)
            },
            product = product,
            onClick = {
                onEvent(CatalogEvent.AddProductToCart(product))
            }
        )
    }
}