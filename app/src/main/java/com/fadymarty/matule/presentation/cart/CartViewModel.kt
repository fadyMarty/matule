package com.fadymarty.matule.presentation.cart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fadymarty.network.domain.model.Cart
import com.fadymarty.network.domain.use_case.bucket.DeleteCartUseCase
import com.fadymarty.network.domain.use_case.bucket.GetBucketUseUse
import com.fadymarty.network.domain.use_case.bucket.UpdateCartUseCase
import com.fadymarty.network.domain.use_case.order.CreateOrderUseCase
import com.fadymarty.network.domain.use_case.shop.GetCatalogUseCase
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CartViewModel(
    private val getBucketUseUse: GetBucketUseUse,
    private val deleteCartUseCase: DeleteCartUseCase,
    private val updateCartUseCase: UpdateCartUseCase,
    private val getCatalogUseCase: GetCatalogUseCase,
    private val createOrderUseCase: CreateOrderUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow(CartState())
    val state = _state.asStateFlow()

    private val _event: Channel<CartEvent> = Channel()
    val event = _event.receiveAsFlow()

    init {
        initialize()
    }

    fun onEvent(event: CartEvent) {
        when (event) {
            is CartEvent.DeleteCart -> {
                deleteCart(event.cart)
            }

            is CartEvent.CountChanged -> {
                onCountChanged(event.cart)
            }

            is CartEvent.ClearBucket -> {
                clearBucket()
            }

            is CartEvent.CreateOrder -> {
                createOrder()
            }

            else -> Unit
        }
    }

    private fun createOrder() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            createOrderUseCase(_state.value.bucket)
                .onSuccess {
                    _state.value.bucket.forEach { cart ->
                        cart.id?.let { id ->
                            deleteCartUseCase(id)
                                .onFailure {
                                    _event.send(CartEvent.ShowErrorSnackBar)
                                }
                        }
                    }
                    _state.update { it.copy(isLoading = false) }
                    _event.send(CartEvent.ShowSuccessSnackBar)
                }
                .onFailure {
                    _state.update { it.copy(isLoading = false) }
                    _event.send(CartEvent.ShowErrorSnackBar)
                }
        }
    }

    private fun clearBucket() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            _state.value.bucket.forEach { cart ->
                cart.id?.let { id ->
                    deleteCartUseCase(id)
                        .onSuccess {
                            _state.update { it.copy(bucket = emptyList()) }
                        }
                        .onFailure {
                            _event.send(CartEvent.ShowErrorSnackBar)
                        }
                }
            }
            _state.update { it.copy(isLoading = false) }
        }
    }

    private fun onCountChanged(cart: Cart) {
        viewModelScope.launch {
            _state.update {
                it.copy(
                    bucket = it.bucket.map { item ->
                        if (item.id == cart.id) cart else item
                    }
                )
            }
            updateCartUseCase(cart)
                .onFailure {
                    _state.update { it.copy(isLoading = false) }
                    _event.send(CartEvent.ShowErrorSnackBar)
                }
        }
    }

    private fun deleteCart(cart: Cart) {
        cart.id?.let { id ->
            viewModelScope.launch {
                _state.update { it.copy(isLoading = true) }
                deleteCartUseCase(id)
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
                        _event.send(CartEvent.ShowErrorSnackBar)
                    }
            }
        }
    }

    private fun initialize() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }

            val bucket = async { getBucketUseUse() }
            val catalog = async { getCatalogUseCase() }

            val bucketResult = bucket.await()
            val catalogResult = catalog.await()

            val results = listOf(bucketResult, catalogResult)

            bucketResult.onSuccess { bucket ->
                _state.update { it.copy(bucket = bucket) }
            }

            catalogResult.onSuccess { catalog ->
                _state.update { it.copy(catalog = catalog) }
            }

            if (results.any { it.isSuccess }) {
                _state.update { it.copy(isLoading = false) }
            } else {
                _event.send(CartEvent.ShowErrorSnackBar)
            }
        }
    }
}