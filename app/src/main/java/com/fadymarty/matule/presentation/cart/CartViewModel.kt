package com.fadymarty.matule.presentation.cart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fadymarty.network.domain.model.Cart
import com.fadymarty.network.domain.use_case.bucket.DeleteCartUseCase
import com.fadymarty.network.domain.use_case.bucket.GetBucketUseUse
import com.fadymarty.network.domain.use_case.bucket.UpdateCartUseCase
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
) : ViewModel() {

    private val _state = MutableStateFlow(CartState())
    val state = _state.asStateFlow()

    private val _event: Channel<CartEvent> = Channel()
    val event = _event.receiveAsFlow()

    init {
        getBucket()
    }

    fun onEvent(event: CartEvent) {
        when (event) {
            is CartEvent.DeleteCart -> {
                deleteCart(event.cart)
            }

            is CartEvent.CountChanged -> {
                onCountChanged(event.cart)
            }

            else -> {}
        }
    }

    private fun onCountChanged(cart: Cart) {
        viewModelScope.launch {
            if (cart.id != null) {
                _state.update { it.copy(isLoading = true) }
                if (cart.count < 1) {
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
                            _event.send(CartEvent.ShowSnackBar)
                        }
                } else {
                    _state.update {
                        it.copy(
                            bucket = it.bucket.map { item ->
                                if (item.id == cart.id) cart else item
                            }
                        )
                    }

                    updateCartUseCase(cart.id!!, cart)
                        .onFailure {
                            _state.update { it.copy(isLoading = false) }
                            _event.send(CartEvent.ShowSnackBar)
                        }
                }
            }
        }
    }

    private fun deleteCart(cart: Cart) {
        if (cart.id != null) {
            viewModelScope.launch {
                _state.update { it.copy(isLoading = true) }
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
                        _event.send(CartEvent.ShowSnackBar)
                    }
            }
        }
    }

    private fun getBucket() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            getBucketUseUse()
                .onSuccess { bucket ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            bucket = bucket
                        )
                    }
                }
                .onFailure {
                    _state.update { it.copy(isLoading = false) }
                    _event.send(CartEvent.ShowSnackBar)
                }
        }
    }
}