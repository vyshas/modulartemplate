package com.example.feature.product.impl.ui.productlist

import com.example.feature.product.api.domain.model.Product

sealed class ProductUiState {
    object Loading : ProductUiState()
    data class Success(val products: List<Product>) : ProductUiState()
    data class Error(val message: String) : ProductUiState()
}