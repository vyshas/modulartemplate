package com.example.feature.product.api.data

import com.example.core.domain.DomainResult
import com.example.feature.product.api.domain.model.Product

interface ProductRepository {
    suspend fun getProducts(): DomainResult<List<Product>>
}