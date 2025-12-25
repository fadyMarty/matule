package com.fadymarty.matule.presentation.catalog

import com.fadymarty.network.domain.model.Product

sealed interface CatalogEvent {
    data class SearchQueryChanged(val value: String) : CatalogEvent
    data object ClearSearchQuery : CatalogEvent
    data class SelectType(val type: String?) : CatalogEvent
    data class ShowProductModal(val product: Product) : CatalogEvent
    data object HideProductModal : CatalogEvent
    data class AddProductToCart(val product: Product) : CatalogEvent
    data object NavigateToProfile : CatalogEvent
    data object NavigateToCart : CatalogEvent
    data object ShowErrorSnackBar : CatalogEvent
}