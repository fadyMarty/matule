package com.fadymarty.matule.presentation.catalog

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fadymarty.network.domain.model.Product
import com.fadymarty.network.domain.use_case.bucket.AddProductToBucketUseCase
import com.fadymarty.network.domain.use_case.bucket.DeleteCartUseCase
import com.fadymarty.network.domain.use_case.bucket.GetBucketUseUse
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

class CatalogViewModel(
    private val searchProductsUseCase: SearchProductsUseCase,
    private val getBucketUseUse: GetBucketUseUse,
    private val addProductToBucketUseCase: AddProductToBucketUseCase,
    private val deleteCartUseCase: DeleteCartUseCase,
) : ViewModel() {
    private val _state = MutableStateFlow(CatalogState())
    val state = _state.asStateFlow()

    private val _event: Channel<CatalogEvent> = Channel()
    val event = _event.receiveAsFlow()

    private var searchJob: Job? = null

    init {
        initialize()
    }

    fun initialize() {
        _state.update { it.copy(isLoading = true) }

        viewModelScope.launch {
            val products = async {
                searchProductsUseCase(
                    query = _state.value.searchQuery,
                    type = _state.value.selectedType
                )
            }
            val bucket = async { getBucketUseUse() }

            val productsResult = products.await()
            val bucketResult = bucket.await()

            val results = listOf(
                productsResult,
                bucketResult
            )

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
                _event.send(CatalogEvent.ShowSnackBar)
            }
        }
    }

    fun onEvent(event: CatalogEvent) {
        when (event) {
            is CatalogEvent.SearchQueryChanged -> {
                _state.update { it.copy(searchQuery = event.value) }
                searchProducts()
            }

            is CatalogEvent.SelectProduct -> {
                selectProduct(product = event.product)
            }

            is CatalogEvent.SelectType -> {
                _state.update { it.copy(selectedType = event.type) }
                searchProducts()
            }

            is CatalogEvent.AddProductToBucket -> {
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
                        _event.send(CatalogEvent.ShowSnackBar)
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
                    _event.send(CatalogEvent.ShowSnackBar)
                }
        }
    }

    private fun addProductToCart(product: Product) {
        _state.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            addProductToBucketUseCase(product)
                .onSuccess { cart ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            selectedProduct = null,
                            bucket = it.bucket + cart
                        )
                    }
                }
                .onFailure {
                    _state.update { it.copy(isLoading = false) }
                    _event.send(CatalogEvent.ShowSnackBar)
                }
        }
    }
}