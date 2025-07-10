package com.example.feature.product.impl.ui.productlist

sealed interface ProductUiIntent {
    object FetchProducts : ProductUiIntent
}