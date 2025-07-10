package com.example.feature.product.impl.repository

import com.example.core.domain.DomainResult
import com.example.feature.product.api.domain.model.Product
import com.example.feature.product.api.data.ProductRepository
import com.example.feature.product.impl.data.ApiProductMapper
import com.example.feature.product.impl.remote.ProductApi
import javax.inject.Inject

class ProductRepositoryImpl @Inject constructor(
    private val api: ProductApi,
    private val mapper: ApiProductMapper
) : ProductRepository {

    override suspend fun getProducts(): DomainResult<List<Product>> {
        return try {
            val apiProducts = api.getProducts()
            val products = apiProducts.map { mapper.map(it) }
            DomainResult.Success(products)
        } catch (e: Exception) {
            DomainResult.Error("Failed to fetch products: ${e.message}")
        }
    }
}