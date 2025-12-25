package com.fadymarty.matule.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fadymarty.network.domain.model.Product
import com.fadymarty.network.domain.use_case.cart.AddProductToCartUseCase
import com.fadymarty.network.domain.use_case.cart.DeleteCartUseCase
import com.fadymarty.network.domain.use_case.cart.GetCartsUseCase
import com.fadymarty.network.domain.use_case.cart.ObserveCartsUseCase
import com.fadymarty.network.domain.use_case.shop.GetNewsUseCase
import com.fadymarty.network.domain.use_case.shop.SearchProductsUseCase
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeViewModel(
    private val getNewsUseCase: GetNewsUseCase,
    private val searchProductsUseCase: SearchProductsUseCase,
    private val getCartsUseCase: GetCartsUseCase,
    private val observeCartsUseCase: ObserveCartsUseCase,
    private val addProductToCartUseCase: AddProductToCartUseCase,
    private val deleteCartUseCase: DeleteCartUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow(HomeState())
    val state = _state.asStateFlow()

    private val eventChannel = Channel<HomeEvent>()
    val events = eventChannel.receiveAsFlow()

    private var searchJob: Job? = null

    init {
        _state.update { it.copy(isLoading = true) }

        observeCartsUseCase().onEach { carts ->
            _state.update { it.copy(carts = carts) }
        }.launchIn(viewModelScope)

        viewModelScope.launch {
            val news = async {
                getNewsUseCase()
                    .onSuccess { news ->
                        _state.update { it.copy(news = news) }
                    }
            }
            val products = async {
                searchProductsUseCase(
                    query = _state.value.searchQuery
                ).onSuccess { products ->
                    _state.update {
                        it.copy(
                            products = products,
                            types = products.map { product ->
                                product.typeCloses
                            }.distinct()
                        )
                    }
                }
            }
            val carts = async { getCartsUseCase() }

            val newsResult = news.await()
            val productsResult = products.await()
            val cartsResult = carts.await()
            val results = listOf(newsResult, productsResult, cartsResult)
            if (results.any { it.isSuccess }) {
                _state.update { it.copy(isLoading = false) }
            } else {
                eventChannel.send(HomeEvent.ShowErrorSnackBar)
            }
        }
    }

    fun onEvent(event: HomeEvent) {
        when (event) {
            is HomeEvent.SearchQueryChanged -> {
                _state.update { it.copy(searchQuery = event.value) }
                searchProducts()
            }

            HomeEvent.ClearSearchQuery -> {
                _state.update { it.copy(searchQuery = "") }
                searchProducts()
            }

            is HomeEvent.SelectType -> {
                _state.update { it.copy(type = event.type) }
            }

            is HomeEvent.AddProductToCart -> {
                addProductToCart(event.product)
            }

            is HomeEvent.ShowProductModal -> {
                _state.update { it.copy(product = event.product) }
            }

            HomeEvent.HideProductModal -> {
                _state.update { it.copy(product = null) }
            }

            else -> Unit
        }
    }

    private fun searchProducts() {
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(500)
            _state.update { it.copy(isLoading = true) }
            searchProductsUseCase(
                query = _state.value.searchQuery
            )
                .onSuccess { products ->
                    _state.update {
                        it.copy(
                            products = products,
                            types = products.map { product ->
                                product.typeCloses
                            }.distinct()
                        )
                    }
                }
                .onFailure {
                    eventChannel.send(HomeEvent.ShowErrorSnackBar)
                }
            _state.update { it.copy(isLoading = false) }
        }
    }

    private fun addProductToCart(product: Product) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            val cart = _state.value.carts.firstOrNull {
                it.productId == product.id
            }
            if (cart != null) {
                deleteCartUseCase(cart.id!!)
                    .onSuccess {
                        _state.update { it.copy(product = null) }
                    }
                    .onFailure {
                        eventChannel.send(HomeEvent.ShowErrorSnackBar)
                    }
            } else {
                addProductToCartUseCase(product)
                    .onSuccess {
                        _state.update { it.copy(product = null) }
                    }
                    .onFailure {
                        eventChannel.send(HomeEvent.ShowErrorSnackBar)
                    }
            }
            _state.update { it.copy(isLoading = false) }
        }
    }
}