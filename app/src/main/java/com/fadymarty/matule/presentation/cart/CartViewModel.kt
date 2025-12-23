package com.fadymarty.matule.presentation.cart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fadymarty.network.domain.model.Cart
import com.fadymarty.network.domain.use_case.cart.DeleteCartUseCase
import com.fadymarty.network.domain.use_case.cart.GetCartsUseCase
import com.fadymarty.network.domain.use_case.cart.ObserveCartsUseCase
import com.fadymarty.network.domain.use_case.cart.UpdateCartUseCase
import com.fadymarty.network.domain.use_case.order.CreateOrderUseCase
import com.fadymarty.network.domain.use_case.shop.GetCatalogUseCase
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CartViewModel(
    private val observeCartsUseCase: ObserveCartsUseCase,
    private val getCartsUseUse: GetCartsUseCase,
    private val deleteCartUseCase: DeleteCartUseCase,
    private val updateCartUseCase: UpdateCartUseCase,
    private val getCatalogUseCase: GetCatalogUseCase,
    private val createOrderUseCase: CreateOrderUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow(CartState())
    val state = _state.asStateFlow()

    private val eventChannel = Channel<CartEvent>()
    val events = eventChannel.receiveAsFlow()

    init {
        initialize()
    }

    private fun initialize() {
        _state.update { it.copy(isLoading = true) }

        observeCartsUseCase().onEach { carts ->
            _state.update { it.copy(carts = carts) }
        }.launchIn(viewModelScope)

        viewModelScope.launch {
            val carts = async { getCartsUseUse() }
            val products = async { getCatalogUseCase() }

            carts.await()
                .onFailure {
                    eventChannel.send(CartEvent.ShowErrorSnackBar)
                }
            products.await()
                .onSuccess { catalog ->
                    _state.update { it.copy(products = catalog) }
                }
                .onFailure {
                    eventChannel.send(CartEvent.ShowErrorSnackBar)
                }

            _state.update { it.copy(isLoading = false) }
        }
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
            createOrderUseCase(_state.value.carts)
                .onSuccess {
                    _state.value.carts.forEach { cart ->
                        deleteCartUseCase(cart.id!!)
                            .onFailure {
                                eventChannel.send(CartEvent.ShowErrorSnackBar)
                            }
                    }
                    _state.update { it.copy(isLoading = false) }
                    eventChannel.send(CartEvent.ShowSuccessSnackBar)
                }
                .onFailure {
                    _state.update { it.copy(isLoading = false) }
                    eventChannel.send(CartEvent.ShowErrorSnackBar)
                }
        }
    }

    private fun clearBucket() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            _state.value.carts.forEach { cart ->
                deleteCartUseCase(cart.id!!)
                    .onSuccess {
                        _state.update { it.copy(carts = emptyList()) }
                    }
                    .onFailure {
                        eventChannel.send(CartEvent.ShowErrorSnackBar)
                    }
            }
            _state.update { it.copy(isLoading = false) }
        }
    }

    private fun onCountChanged(cart: Cart) {
        viewModelScope.launch {
            _state.update {
                it.copy(
                    carts = it.carts.map { item ->
                        if (item.id == cart.id) cart else item
                    }
                )
            }
            updateCartUseCase(cart)
                .onFailure {
                    _state.update { it.copy(isLoading = false) }
                    eventChannel.send(CartEvent.ShowErrorSnackBar)
                }
        }
    }

    private fun deleteCart(cart: Cart) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            deleteCartUseCase(cart.id!!)
                .onSuccess {
                    _state.update { it.copy(isLoading = false) }
                }
                .onFailure {
                    _state.update { it.copy(isLoading = false) }
                    eventChannel.send(CartEvent.ShowErrorSnackBar)
                }
        }
    }
}