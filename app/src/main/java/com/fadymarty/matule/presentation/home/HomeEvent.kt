package com.fadymarty.matule.presentation.home

import com.fadymarty.network.domain.model.Product

sealed class HomeEvent {
    data class SearchQueryChanged(val value: String) : HomeEvent()
    data class SelectType(val type: String?) : HomeEvent()
    data class SelectProduct(val product: Product?) : HomeEvent()
    data class AddProductToBucket(val product: Product) : HomeEvent()
    object ShowSnackBar : HomeEvent()
}