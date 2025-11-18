package com.fadymarty.matule.presentation.cart

import com.fadymarty.network.domain.model.Cart

sealed class CartEvent {
    data class DeleteCart(val cart: Cart) : CartEvent()
    data class CountChanged(val cart: Cart) : CartEvent()
    data object ShowSnackBar : CartEvent()
}