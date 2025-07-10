package com.example.feature.product.impl.ui.productlist

sealed interface ProductUiEffect {
    data class ShowToast(val message: String) : ProductUiEffect
}