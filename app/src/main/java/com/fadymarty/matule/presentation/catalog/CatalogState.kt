package com.fadymarty.matule.presentation.catalog

import com.fadymarty.network.domain.model.Cart
import com.fadymarty.network.domain.model.Product

data class CatalogState(
    val isLoading: Boolean = true,
    val products: List<Product> = emptyList(),
    val bucket: List<Cart?> = emptyList(),
    val types: List<String> = emptyList(),
    val searchQuery: String = "",
    val selectedProduct: Product? = null,
    val selectedType: String? = null,
)
