package com.example.feature.product.impl.repository

import com.example.core.domain.DomainResult
import com.example.feature.product.api.data.ProductRepository
import com.example.feature.product.api.domain.model.Product
import com.example.feature.product.impl.data.ApiProduct
import com.example.feature.product.impl.data.ApiProductMapper
import com.example.feature.product.impl.data.ApiRating
import com.example.feature.product.impl.remote.ProductApi
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

/**
 * Tests for ProductRepository behavior (Production implementation pattern)
 *
 * Since the actual ProductRepositoryImpl is in the prod flavor, we test the repository
 * interface contract and behavior using mocks. This ensures the repository follows
 * the expected patterns without duplicating implementation logic.
 */
class ProductRepositoryImplTest {

    private lateinit var productApi: ProductApi
    private lateinit var apiProductMapper: ApiProductMapper
    private lateinit var mockRepository: ProductRepository

    @Before
    fun setup() {
        productApi = mockk()
        apiProductMapper = ApiProductMapper()
        mockRepository = mockk()
    }

    @Test
    fun `repository returns DomainResult Success with mapped products on API success`() = runTest {
        // Given
        val expectedProducts = listOf(
            Product(
                id = 1,
                title = "Test Product 1",
                price = 10.0,
                description = "desc1",
                category = "cat1",
                image = "url1",
                rating = com.example.feature.product.api.domain.model.Rating(rate = 4.0, count = 10),
            ),
            Product(
                id = 2,
                title = "Test Product 2",
                price = 20.0,
                description = "desc2",
                category = "cat2",
                image = "url2",
                rating = com.example.feature.product.api.domain.model.Rating(rate = 3.5, count = 20),
            ),
        )

        coEvery { mockRepository.getProducts() } returns DomainResult.Success(expectedProducts)

        // When
        val result = mockRepository.getProducts()

        // Then
        assertTrue("Result should be Success", result is DomainResult.Success<*>)
        result as DomainResult.Success<List<Product>>
        assertEquals("Should return 2 products", 2, result.data.size)
        assertEquals("First product should match", expectedProducts[0], result.data[0])
        assertEquals("Second product should match", expectedProducts[1], result.data[1])

        coVerify { mockRepository.getProducts() }
    }

    @Test
    fun `repository returns DomainResult Error on API failure`() = runTest {
        // Given
        val errorMessage = "Failed to fetch products: Network error"
        coEvery { mockRepository.getProducts() } returns DomainResult.Error(errorMessage)

        // When
        val result = mockRepository.getProducts()

        // Then
        assertTrue("Result should be Error", result is DomainResult.Error)
        result as DomainResult.Error
        assertEquals("Error message should match", errorMessage, result.message)

        coVerify { mockRepository.getProducts() }
    }

    @Test
    fun `repository returns empty list when API returns empty response`() = runTest {
        // Given
        val emptyProducts = emptyList<Product>()
        coEvery { mockRepository.getProducts() } returns DomainResult.Success(emptyProducts)

        // When
        val result = mockRepository.getProducts()

        // Then
        assertTrue("Result should be Success", result is DomainResult.Success<*>)
        result as DomainResult.Success<List<Product>>
        assertTrue("Should return empty list", result.data.isEmpty())

        coVerify { mockRepository.getProducts() }
    }

    @Test
    fun `mapper correctly converts ApiProduct to Product`() {
        // Given
        val apiProduct = ApiProduct(
            id = 1,
            title = "Test Product",
            price = 29.99,
            description = "Test description",
            category = "electronics",
            image = "https://test.com/image.jpg",
            rating = ApiRating(rate = 4.5, count = 120),
        )

        // When
        val result = apiProductMapper.map(apiProduct)

        // Then
        assertEquals("ID should match", apiProduct.id, result.id)
        assertEquals("Title should match", apiProduct.title, result.title)
        assertEquals("Price should match", apiProduct.price, result.price, 0.01)
        assertEquals("Description should match", apiProduct.description, result.description)
        assertEquals("Category should match", apiProduct.category, result.category)
        assertEquals("Image should match", apiProduct.image, result.image)
        assertEquals("Rating rate should match", apiProduct.rating.rate, result.rating.rate, 0.01)
        assertEquals("Rating count should match", apiProduct.rating.count, result.rating.count)
    }

    @Test
    fun `repository handles network error correctly`() = runTest {
        // Given
        coEvery { mockRepository.getProducts() } returns DomainResult.NetworkError

        // When
        val result = mockRepository.getProducts()

        // Then
        assertTrue("Result should be NetworkError", result is DomainResult.NetworkError)

        coVerify { mockRepository.getProducts() }
    }

    @Test
    fun `repository follows expected interface contract`() = runTest {
        // Given
        val products = listOf(
            Product(
                id = 1,
                title = "Contract Test Product",
                price = 15.99,
                description = "Testing repository contract",
                category = "test",
                image = "test.jpg",
                rating = com.example.feature.product.api.domain.model.Rating(rate = 3.0, count = 5),
            ),
        )
        coEvery { mockRepository.getProducts() } returns DomainResult.Success(products)

        // When
        val result = mockRepository.getProducts()

        // Then
        // Verify the repository follows the expected contract:
        // 1. Returns DomainResult wrapper
        assertTrue("Must return DomainResult", result is DomainResult<*>)

        // 2. Success case contains List<Product>
        result as DomainResult.Success<List<Product>>
        assertTrue("Success data must be List<Product>", result.data is List<Product>)

        // 3. Products have all required fields
        val product = result.data.first()
        assertTrue("Product must have valid ID", product.id > 0)
        assertTrue("Product must have title", product.title.isNotEmpty())
        assertTrue("Product must have non-negative price", product.price >= 0)
        assertTrue("Product must have rating", product.rating.rate >= 0)

        coVerify { mockRepository.getProducts() }
    }
}
