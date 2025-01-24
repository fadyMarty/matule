package com.fadymarty.matule.presentation.cart.components

data class CartItem(
    val name: String = "",
    val price: Float = 0f,
    val count: Int = 1,
    val image: Int
)