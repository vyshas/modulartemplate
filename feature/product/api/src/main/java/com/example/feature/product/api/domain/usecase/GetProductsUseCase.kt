package com.example.feature.product.api.domain.usecase

import com.example.core.domain.DomainResult
import com.example.feature.product.api.domain.model.Product

interface GetProductsUseCase {
    suspend fun getProducts(): DomainResult<List<Product>>
}