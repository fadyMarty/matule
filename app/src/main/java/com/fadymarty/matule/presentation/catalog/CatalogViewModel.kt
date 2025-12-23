package com.fadymarty.matule.presentation.catalog

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fadymarty.network.domain.model.Product
import com.fadymarty.network.domain.use_case.cart.AddProductToCartUseCase
import com.fadymarty.network.domain.use_case.cart.DeleteCartUseCase
import com.fadymarty.network.domain.use_case.cart.GetCartsUseCase
import com.fadymarty.network.domain.use_case.cart.ObserveCartsUseCase
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

class CatalogViewModel(
    private val searchProductsUseCase: SearchProductsUseCase,
    private val observeCartsUseCase: ObserveCartsUseCase,
    private val getCartsUseUse: GetCartsUseCase,
    private val addProductToCartUseCase: AddProductToCartUseCase,
    private val deleteCartUseCase: DeleteCartUseCase,
) : ViewModel() {
    
    private val _state = MutableStateFlow(CatalogState())
    val state = _state.asStateFlow()

    private val eventChannel = Channel<CatalogEvent>()
    val events = eventChannel.receiveAsFlow()

    private var searchJob: Job? = null

    init {
        initialize()
    }

    fun initialize() {
        _state.update { it.copy(isLoading = true) }

        observeCartsUseCase().onEach { carts ->
            _state.update { it.copy(carts = carts) }
        }.launchIn(viewModelScope)

        viewModelScope.launch {
            val products = async {
                searchProductsUseCase(
                    query = _state.value.searchQuery,
                    type = _state.value.selectedType
                )
            }
            val carts = async { getCartsUseUse() }

            products.await()
                .onSuccess { products ->
                    _state.update {
                        it.copy(
                            products = products,
                            types = products.map { product -> product.typeCloses }.distinct()
                        )
                    }
                }
                .onFailure {
                    eventChannel.send(CatalogEvent.ShowErrorSnackBar)
                }
            carts.await()
                .onFailure {
                    eventChannel.send(CatalogEvent.ShowErrorSnackBar)
                }

            _state.update { it.copy(isLoading = false) }
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

            else -> Unit
        }
    }

    private fun selectProduct(product: Product?) {
        val cart = _state.value.carts.firstOrNull { it.productId == product?.id }

        if (cart?.id != null) {
            _state.update { it.copy(isLoading = true) }
            viewModelScope.launch {
                deleteCartUseCase(cart.id!!)
                    .onSuccess {
                        _state.update {
                            it.copy(isLoading = false)
                        }
                    }
                    .onFailure {
                        _state.update { it.copy(isLoading = false) }
                        eventChannel.send(CatalogEvent.ShowErrorSnackBar)
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
                                types = products.map { productItem ->
                                    productItem.typeCloses
                                }.distinct()
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
                    eventChannel.send(CatalogEvent.ShowErrorSnackBar)
                }
        }
    }

    private fun addProductToCart(product: Product) {
        _state.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            addProductToCartUseCase(product)
                .onSuccess {
                    _state.update {
                        it.copy(
                            isLoading = false,
                            selectedProduct = null
                        )
                    }
                }
                .onFailure {
                    _state.update { it.copy(isLoading = false) }
                    eventChannel.send(CatalogEvent.ShowErrorSnackBar)
                }
        }
    }
}