package com.fadymarty.matule.presentation.catalog

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Icon
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
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import com.fadymarty.matule.R
import com.fadymarty.matule.presentation.components.LoadingContent
import com.fadymarty.matule.presentation.components.ProductModal
import com.fadymarty.matule.presentation.navigation.Route
import com.fadymarty.matule_ui_kit.presentation.components.buttons.CartButton
import com.fadymarty.matule_ui_kit.presentation.components.buttons.ChipButton
import com.fadymarty.matule_ui_kit.presentation.components.cards.PrimaryCard
import com.fadymarty.matule_ui_kit.presentation.components.input.SearchInput
import com.fadymarty.matule_ui_kit.presentation.components.snack_bar.SnackBar
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@Composable
fun CatalogScreen(
    rootNavController: NavHostController,
    navController: NavHostController,
    viewModel: CatalogViewModel = koinViewModel(),
) {
    val context = LocalContext.current
    val state by viewModel.state.collectAsStateWithLifecycle()
    val snackBarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.event.collect { event ->
            when (event) {
                is CatalogEvent.ShowErrorSnackBar -> {
                    val job = launch {
                        snackBarHostState.showSnackbar(
                            message = context.getString(R.string.error_message),
                            duration = SnackbarDuration.Indefinite
                        )
                    }
                    delay(5000)
                    job.cancel()
                }

                else -> {}
            }
        }
    }

    CatalogContent(
        snackBarHostState = snackBarHostState,
        state = state,
        onEvent = viewModel::onEvent,
        rootNavController = rootNavController,
        navController = navController
    )
}

@Composable
private fun CatalogContent(
    state: CatalogState,
    onEvent: (CatalogEvent) -> Unit,
    snackBarHostState: SnackbarHostState,
    rootNavController: NavHostController,
    navController: NavHostController,
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            Row(
                modifier = Modifier
                    .statusBarsPadding()
                    .padding(top = 28.dp)
                    .padding(horizontal = 20.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                SearchInput(
                    modifier = Modifier.weight(1f),
                    value = state.searchQuery,
                    onValueChange = {
                        onEvent(CatalogEvent.SearchQueryChanged(it))
                    },
                    hint = "Искать описания",
                    onClear = {
                        onEvent(CatalogEvent.SearchQueryChanged(""))
                    }
                )
                Spacer(Modifier.width(38.dp))
                Icon(
                    modifier = Modifier
                        .size(32.dp)
                        .clickable(
                            interactionSource = null,
                            indication = null
                        ) {
                            navController.navigate(Route.ProfileScreen) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                    painter = painterResource(R.drawable.ic_user),
                    contentDescription = null
                )
            }
        },
        snackbarHost = {
            SnackbarHost(
                hostState = snackBarHostState,
                snackbar = {
                    SnackBar(
                        modifier = Modifier.padding(start = 20.dp, end = 8.dp),
                        message = it.visuals.message,
                        onClose = {
                            snackBarHostState.currentSnackbarData?.dismiss()
                        }
                    )
                }
            )
        }
    ) { innerPadding ->
        if (state.isLoading) {
            LoadingContent(
                modifier = Modifier
                    .padding(
                        top = innerPadding.calculateTopPadding() + 24.dp
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
                LazyRow(
                    modifier = Modifier.fillMaxWidth(),
                    contentPadding = PaddingValues(horizontal = 20.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    item {
                        ChipButton(
                            selected = state.selectedType == null,
                            label = "Все",
                            onClick = {
                                onEvent(CatalogEvent.SelectType(null))
                            }
                        )
                    }
                    items(state.types) { type ->
                        ChipButton(
                            selected = type == state.selectedType,
                            label = type,
                            onClick = {
                                onEvent(CatalogEvent.SelectType(type))
                            }
                        )
                    }
                }
                Spacer(Modifier.height(8.dp))
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentPadding = PaddingValues(
                        top = 12.dp,
                        bottom = if (state.bucket.isNotEmpty()) 120.dp else 0.dp
                    )
                ) {
                    items(state.products) { product ->
                        PrimaryCard(
                            modifier = Modifier.padding(horizontal = 20.dp),
                            title = product.title,
                            type = product.type,
                            price = product.price,
                            added = state.bucket.any { it?.productId == product.id },
                            onClick = {
                                onEvent(CatalogEvent.SelectProduct(product))
                            }
                        )
                        Spacer(Modifier.height(16.dp))
                    }
                }
                if (state.bucket.isNotEmpty()) {
                    CartButton(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                horizontal = 20.dp,
                                vertical = 32.dp
                            ),
                        onClick = {
                            rootNavController.navigate(Route.CartScreen)
                        },
                        price = state.bucket.sumOf { cart ->
                            state.products.first {
                                it.id == cart?.productId
                            }.price
                        }
                    )
                }
            }
        }
        state.selectedProduct?.let { product ->
            ProductModal(
                onDismissRequest = {
                    onEvent(CatalogEvent.SelectProduct(null))
                },
                product = product,
                onClick = {
                    onEvent(CatalogEvent.AddProductToBucket(product))
                }
            )
        }
    }
}