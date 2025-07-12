package com.example.feature.product.impl.domain.usecase

import com.example.core.domain.DomainResult
import com.example.feature.product.api.data.ProductRepository
import com.example.feature.product.api.domain.model.Product
import com.example.feature.product.api.domain.usecase.GetProductsUseCase
import javax.inject.Inject

class GetProductsUseCaseImpl @Inject constructor(
    private val repository: ProductRepository
) : GetProductsUseCase {

    override suspend fun getProducts(): DomainResult<List<Product>> {
        return repository.getProducts()
    }
}