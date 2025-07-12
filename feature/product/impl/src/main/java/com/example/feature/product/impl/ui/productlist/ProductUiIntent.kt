package com.example.feature.product.impl.ui.productlist

import com.example.feature.product.api.domain.model.Product

sealed interface ProductUiIntent {
    object FetchProducts : ProductUiIntent
    data class ProductClicked(val productId: Int) : ProductUiIntent
}