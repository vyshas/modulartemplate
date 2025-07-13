package com.example.feature.templatefeature.impl.data

data class ApiTemplateFeature(
    val id: Int,
    val title: String,
    val price: Double,
    val description: String,
    val category: String,
    val image: String,
    val rating: ApiRating
)

data class ApiRating(
    val rate: Double,
    val count: Int
)