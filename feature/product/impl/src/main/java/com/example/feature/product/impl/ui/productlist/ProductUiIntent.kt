package com.example.feature.product.impl.ui.productlist

sealed interface ProductUiIntent {
    object FetchProducts : ProductUiIntent
    data class ProductClicked(val productId: Int) : ProductUiIntent
}
