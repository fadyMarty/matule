package com.fadymarty.matule.presentation.cart

import com.fadymarty.network.domain.model.Cart

data class CartState(
    val isLoading: Boolean = true,
    val bucket: List<Cart> = emptyList(),
)
