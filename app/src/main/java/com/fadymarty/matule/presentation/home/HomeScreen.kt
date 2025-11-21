package com.fadymarty.matule.presentation.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.fadymarty.matule.R
import com.fadymarty.matule.presentation.components.LoadingContent
import com.fadymarty.matule.presentation.components.ProductModal
import com.fadymarty.matule_ui_kit.common.theme.MatuleTheme
import com.fadymarty.matule_ui_kit.presentation.components.buttons.ChipButton
import com.fadymarty.matule_ui_kit.presentation.components.cards.PrimaryCard
import com.fadymarty.matule_ui_kit.presentation.components.input.SearchInput
import com.fadymarty.matule_ui_kit.presentation.components.snack_bar.SnackBar
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val snackBarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.event.collect { event ->
            when (event) {
                is HomeEvent.ShowErrorSnackBar -> {
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

    HomeContent(
        state = state,
        onEvent = viewModel::onEvent,
        snackBarHostState = snackBarHostState
    )
}

@Composable
private fun HomeContent(
    state: HomeState,
    onEvent: (HomeEvent) -> Unit,
    snackBarHostState: SnackbarHostState,
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            SearchInput(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MatuleTheme.colorScheme.background)
                    .statusBarsPadding()
                    .padding(horizontal = 20.dp)
                    .padding(
                        top = 24.dp,
                        bottom = 8.dp
                    ),
                value = state.searchQuery,
                onValueChange = {
                    onEvent(HomeEvent.SearchQueryChanged(it))
                },
                hint = "Искать описания",
                onClear = {
                    onEvent(HomeEvent.SearchQueryChanged(""))
                }
            )
        },
        snackbarHost = {
            SnackbarHost(
                hostState = snackBarHostState,
                snackbar = {
                    SnackBar(
                        modifier = Modifier.padding(start = 20.dp, end = 8.dp),
                        onClose = {
                            snackBarHostState.currentSnackbarData?.dismiss()
                        },
                        message = it.visuals.message
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
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(
                    top = innerPadding.calculateTopPadding() + 24.dp
                )
            ) {
                item {
                    Text(
                        modifier = Modifier.padding(horizontal = 20.dp),
                        text = "Акции и новости",
                        style = MatuleTheme.typography.title3Semibold,
                        color = MatuleTheme.colorScheme.placeholder
                    )
                    Spacer(Modifier.height(16.dp))
                    LazyRow(
                        modifier = Modifier.fillMaxWidth(),
                        contentPadding = PaddingValues(horizontal = 20.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        itemsIndexed(state.news) { index, news ->
                            Box(
                                modifier = Modifier
                                    .size(270.dp, 152.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(
                                        brush = if (index % 2 == 0) {
                                            Brush.horizontalGradient(
                                                colors = listOf(
                                                    Color(0xFF97D9F0),
                                                    Color(0xFF92E9D4)
                                                )
                                            )
                                        } else Brush.horizontalGradient(
                                            colors = listOf(Color(0xFF76B3FF), Color(0xFFCDE3FF))
                                        )
                                    )
                            ) {
                                AsyncImage(
                                    modifier = Modifier.fillMaxSize(),
                                    model = news.newsImage,
                                    contentDescription = null
                                )
                                Column(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(horizontal = 16.dp)
                                        .padding(top = 16.dp, bottom = 12.dp),
                                    verticalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        modifier = Modifier.width(176.dp),
                                        text = news.id,
                                        style = MatuleTheme.typography.title2ExtraBold,
                                        color = MatuleTheme.colorScheme.onAccent
                                    )
                                    Text(
                                        text = news.id,
                                        style = MatuleTheme.typography.title2ExtraBold,
                                        color = MatuleTheme.colorScheme.onAccent
                                    )
                                }
                            }
                        }
                    }
                }
                item {
                    Spacer(Modifier.height(32.dp))
                    Text(
                        modifier = Modifier.padding(horizontal = 21.dp),
                        text = "Каталог описаний"
                    )
                    Spacer(Modifier.height(15.dp))
                    LazyRow(
                        modifier = Modifier.fillMaxWidth(),
                        contentPadding = PaddingValues(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        item {
                            ChipButton(
                                selected = state.selectedType == null,
                                label = "Все",
                                onClick = {
                                    onEvent(HomeEvent.SelectType(null))
                                }
                            )
                        }
                        items(state.types) { type ->
                            ChipButton(
                                selected = type == state.selectedType,
                                label = type,
                                onClick = {
                                    onEvent(HomeEvent.SelectType(type))
                                }
                            )
                        }
                    }
                }
                item {
                    Spacer(Modifier.height(25.dp))
                }
                itemsIndexed(state.products) { index, product ->
                    PrimaryCard(
                        modifier = Modifier.padding(horizontal = 20.dp),
                        title = product.title,
                        type = product.type,
                        price = product.price,
                        added = state.bucket.any { it?.productId == product.id },
                        onClick = {
                            onEvent(HomeEvent.SelectProduct(product))
                        }
                    )
                    Spacer(Modifier.height(16.dp))
                }
            }
        }
        state.selectedProduct?.let { product ->
            ProductModal(
                onDismissRequest = {
                    onEvent(HomeEvent.SelectProduct(null))
                },
                product = product,
                onClick = {
                    onEvent(HomeEvent.AddProductToBucket(product))
                }
            )
        }
    }
}