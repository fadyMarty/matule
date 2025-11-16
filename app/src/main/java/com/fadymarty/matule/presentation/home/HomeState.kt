package com.fadymarty.matule.presentation.home

import com.fadymarty.network.domain.model.Cart
import com.fadymarty.network.domain.model.News
import com.fadymarty.network.domain.model.Product

data class HomeState(
    val isLoading: Boolean = true,
    val isCatalogLoading: Boolean = true,
    val isAddProductToCartLoading: Boolean = true,
    val news: List<News> = emptyList(),
    val products: List<Product> = emptyList(),
    val bucket: List<Cart?> = emptyList(),
    val types: List<String> = emptyList(),
    val searchQuery: String = "",
    val selectedProduct: Product? = null,
    val selectedType: String? = null,
)