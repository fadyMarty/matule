package com.fadymarty.matule.presentation.catalog

import com.fadymarty.network.domain.model.Product

sealed class CatalogEvent {
    data class SearchQueryChanged(val value: String) : CatalogEvent()
    data class SelectType(val type: String?) : CatalogEvent()
    data class SelectProduct(val product: Product?) : CatalogEvent()
    data class AddProductToBucket(val product: Product) : CatalogEvent()
    object ShowSnackBar : CatalogEvent()
}