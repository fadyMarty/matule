package com.fadymarty.matule.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fadymarty.network.domain.model.Product
import com.fadymarty.network.domain.use_case.bucket.AddProductToCartUseCase
import com.fadymarty.network.domain.use_case.bucket.GetBucketUseUse
import com.fadymarty.network.domain.use_case.shop.GetNewsUseCase
import com.fadymarty.network.domain.use_case.shop.SearchProductsUseCase
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeViewModel(
    private val getNewsUseCase: GetNewsUseCase,
    private val searchProductsUseCase: SearchProductsUseCase,
    private val getBucketUseUse: GetBucketUseUse,
    private val addProductToBucketUseCase: AddProductToCartUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow(HomeState())
    val state = _state.asStateFlow()

    private val _event: Channel<HomeEvent> = Channel()
    val event = _event.receiveAsFlow()

    private var searchJob: Job? = null

    init {
        initialize()
    }

    fun initialize() {
        _state.update { it.copy(isLoading = true) }

        viewModelScope.launch {
            val news = async { getNewsUseCase() }
            val catalog = async {
                searchProductsUseCase(
                    query = _state.value.searchQuery,
                    type = _state.value.selectedType
                )
            }
            val bucket = async { getBucketUseUse() }

            val newsResult = news.await()
            val catalogResult = catalog.await()
            val bucketResult = bucket.await()

            val results = listOf(
                newsResult,
                catalogResult,
                bucketResult
            )

            newsResult.onSuccess { news ->
                _state.update { it.copy(news = news) }
            }

            catalogResult.onSuccess { products ->
                _state.update {
                    it.copy(
                        products = products,
                        types = products.map { product -> product.typeCloses }.distinct()
                    )
                }
            }

            bucketResult.onSuccess { bucket ->
                _state.update { it.copy(bucket = bucket) }
            }

            if (results.all { it.isSuccess }) {
                _state.update { it.copy(isLoading = false) }
            } else {
                _event.send(HomeEvent.ShowSnackBar)
            }
        }
    }

    fun onEvent(event: HomeEvent) {
        when (event) {
            is HomeEvent.SearchQueryChanged -> {
                _state.update { it.copy(searchQuery = event.value) }
                searchProducts()
            }

            is HomeEvent.SelectProduct -> {
                _state.update { it.copy(selectedProduct = event.product) }
            }

            is HomeEvent.SelectType -> {
                _state.update { it.copy(selectedType = event.type) }
                searchProducts()
            }

            is HomeEvent.AddProductToBucket -> {
                addProductToCart(event.product)
            }

            else -> {}
        }
    }

    private fun searchProducts() {
        _state.update { it.copy(isCatalogLoading = true) }
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(1000L)
            searchProductsUseCase(
                query = _state.value.searchQuery,
                type = _state.value.selectedType
            )
                .onSuccess { products ->
                    if (_state.value.searchQuery.isEmpty() && _state.value.selectedType == null) {
                        _state.update {
                            it.copy(
                                isCatalogLoading = false,
                                products = products,
                                types = products.map { productItem -> productItem.typeCloses }
                                    .distinct()
                            )
                        }
                    } else {
                        _state.update {
                            it.copy(
                                isCatalogLoading = false,
                                products = products
                            )
                        }
                    }
                }
                .onFailure {
                    _event.send(HomeEvent.ShowSnackBar)
                }
        }
    }

    private fun addProductToCart(product: Product) {
        _state.update { it.copy(isAddProductToCartLoading = true) }
        viewModelScope.launch {
            addProductToBucketUseCase(product)
                .onSuccess { cart ->
                    _state.update {
                        it.copy(
                            isAddProductToCartLoading = false,
                            selectedProduct = null,
                            bucket = it.bucket + cart
                        )
                    }
                }
                .onFailure {
                    _state.update { it.copy(isAddProductToCartLoading = false) }
                    _event.send(HomeEvent.ShowSnackBar)
                }
        }
    }
}