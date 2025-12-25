package com.fadymarty.matule.presentation.home

import com.fadymarty.network.domain.model.Product

sealed interface HomeEvent {
    data class SearchQueryChanged(val value: String) : HomeEvent
    data object ClearSearchQuery : HomeEvent
    data class SelectType(val type: String?) : HomeEvent
    data class AddProductToCart(val product: Product) : HomeEvent
    data class ShowProductModal(val product: Product) : HomeEvent
    data object HideProductModal : HomeEvent
    data object ShowErrorSnackBar : HomeEvent
}