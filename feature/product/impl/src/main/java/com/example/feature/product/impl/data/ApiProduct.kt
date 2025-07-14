package com.example.feature.product.impl.data

data class ApiProduct(
    val id: Int,
    val title: String,
    val price: Double,
    val description: String,
    val category: String,
    val image: String,
    val rating: ApiRating,
)

data class ApiRating(
    val rate: Double,
    val count: Int,
)
