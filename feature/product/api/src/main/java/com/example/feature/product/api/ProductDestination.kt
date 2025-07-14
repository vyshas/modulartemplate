package com.example.feature.product.api

sealed interface ProductDestination {
    data class Detail(val productId: Int) : ProductDestination
}
