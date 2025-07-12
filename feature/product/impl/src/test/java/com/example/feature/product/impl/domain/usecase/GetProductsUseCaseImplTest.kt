package com.example.feature.product.impl.domain.usecase

import com.example.core.domain.DomainResult
import com.example.feature.product.api.data.ProductRepository
import com.example.feature.product.api.domain.model.Product
import com.example.feature.product.api.domain.model.Rating
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue

class GetProductsUseCaseImplTest {

    private lateinit var productRepository: ProductRepository
    private lateinit var getProductsUseCase: GetProductsUseCaseImpl

    @Before
    fun setup() {
        productRepository = mockk()
        getProductsUseCase = GetProductsUseCaseImpl(productRepository)
    }

    @Test
    fun `getProducts returns products from repository on success`() = runTest {
        // Given
        val expectedProducts = listOf(
            Product(id = 1, title = "Product A", price = 10.0, description = "descA", category = "catA", image = "urlA", rating = Rating(rate = 4.0, count = 10)),
            Product(id = 2, title = "Product B", price = 20.0, description = "descB", category = "catB", image = "urlB", rating = Rating(rate = 3.5, count = 20))
        )
        coEvery { productRepository.getProducts() } returns DomainResult.Success(expectedProducts)

        // When
        val result = getProductsUseCase.getProducts()

        // Then
        assertTrue(result is DomainResult.Success)
        result as DomainResult.Success
        assertEquals(expectedProducts, result.data)
    }

    @Test
    fun `getProducts returns error when repository returns error`() = runTest {
        // Given
        val errorMessage = "Repository Error"
        coEvery { productRepository.getProducts() } returns DomainResult.Error(errorMessage)

        // When
        val result = getProductsUseCase.getProducts()

        // Then
        assertTrue(result is DomainResult.Error)
        result as DomainResult.Error
        assertEquals(errorMessage, result.message)
    }
}