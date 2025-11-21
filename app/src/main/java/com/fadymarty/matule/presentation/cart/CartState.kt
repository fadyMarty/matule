package com.fadymarty.matule.presentation.cart

import com.fadymarty.network.domain.model.Cart
import com.fadymarty.network.domain.model.Product

data class CartState(
    val isLoading: Boolean = true,
    val bucket: List<Cart> = emptyList(),
    val catalog: List<Product> = emptyList(),
)
