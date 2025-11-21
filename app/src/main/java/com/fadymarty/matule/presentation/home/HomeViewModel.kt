package com.fadymarty.matule.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fadymarty.network.domain.model.Product
import com.fadymarty.network.domain.use_case.bucket.AddProductToBucketUseCase
import com.fadymarty.network.domain.use_case.bucket.DeleteCartUseCase
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
    private val addProductToBucketUseCase: AddProductToBucketUseCase,
    private val deleteCartUseCase: DeleteCartUseCase,
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
            val products = async {
                searchProductsUseCase(
                    query = _state.value.searchQuery,
                    type = _state.value.selectedType
                )
            }
            val bucket = async { getBucketUseUse() }

            val newsResult = news.await()
            val productsResult = products.await()
            val bucketResult = bucket.await()

            val results = listOf(
                newsResult,
                productsResult,
                bucketResult
            )

            newsResult.onSuccess { news ->
                _state.update { it.copy(news = news) }
            }

            productsResult.onSuccess { products ->
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
                _state.update { it.copy(isLoading = false) }
                _event.send(HomeEvent.ShowErrorSnackBar)
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
                selectProduct(event.product)
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

    private fun selectProduct(product: Product?) {
        val cart = _state.value.bucket.firstOrNull { it?.productId == product?.id }

        if (cart?.id != null) {
            _state.update { it.copy(isLoading = true) }
            viewModelScope.launch {
                deleteCartUseCase(cart.id!!)
                    .onSuccess {
                        _state.update {
                            it.copy(
                                isLoading = false,
                                bucket = it.bucket - cart
                            )
                        }
                    }
                    .onFailure {
                        _state.update { it.copy(isLoading = false) }
                        _event.send(HomeEvent.ShowErrorSnackBar)
                    }
            }
        } else {
            _state.update { it.copy(selectedProduct = product) }
        }
    }

    private fun searchProducts() {
        _state.update { it.copy(isLoading = true) }
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
                                isLoading = false,
                                products = products,
                                types = products.map { productItem -> productItem.typeCloses }
                                    .distinct()
                            )
                        }
                    } else {
                        _state.update {
                            it.copy(
                                isLoading = false,
                                products = products
                            )
                        }
                    }
                }
                .onFailure {
                    _event.send(HomeEvent.ShowErrorSnackBar)
                }
        }
    }

    private fun addProductToCart(product: Product) {
        _state.update {
            it.copy(
                isLoading = true,
                selectedProduct = null
            )
        }
        viewModelScope.launch {
            addProductToBucketUseCase(product)
                .onSuccess { cart ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            bucket = it.bucket + cart
                        )
                    }
                }
                .onFailure {
                    _state.update { it.copy(isLoading = false) }
                    _event.send(HomeEvent.ShowErrorSnackBar)
                }
        }
    }
}