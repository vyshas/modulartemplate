package com.example.feature.product.impl.repository

import android.content.Context
import com.example.core.domain.DomainResult
import com.example.feature.product.api.domain.model.Product
import com.example.feature.product.impl.data.ApiProductMapper
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import java.io.InputStream

/**
 * Tests for ProductRepositoryMockImpl
 *
 * Tests the mock repository implementation that reads from local JSON assets.
 */
class ProductRepositoryMockImplTest {

    private lateinit var context: Context
    private lateinit var apiProductMapper: ApiProductMapper
    private lateinit var mockRepository: ProductRepositoryMockImpl

    private val mockJsonData = """
        [
            {
                "id": 1,
                "title": "Test Product 1",
                "price": 10.99,
                "description": "Test description 1",
                "category": "test category",
                "image": "https://test.com/image1.jpg",
                "rating": {
                    "rate": 4.5,
                    "count": 100
                }
            },
            {
                "id": 2,
                "title": "Test Product 2",
                "price": 20.99,
                "description": "Test description 2",
                "category": "test category 2",
                "image": "https://test.com/image2.jpg",
                "rating": {
                    "rate": 3.8,
                    "count": 50
                }
            }
        ]
    """.trimIndent()

    @Before
    fun setup() {
        context = mockk()
        apiProductMapper = ApiProductMapper()

        // Mock the assets reading
        val inputStream = mockJsonData.byteInputStream()
        every { context.assets.open("product_mock.json") } returns inputStream

        mockRepository = ProductRepositoryMockImpl(context, apiProductMapper)
    }

    @Test
    fun `getProducts returns mapped products from JSON on success`() = runTest {
        // When
        val result = mockRepository.getProducts()

        // Then
        assertTrue("Result should be Success", result is DomainResult.Success<*>)
        result as DomainResult.Success<List<Product>>

        assertEquals("Should return 2 products", 2, result.data.size)

        val firstProduct = result.data[0]
        assertEquals(1, firstProduct.id)
        assertEquals("Test Product 1", firstProduct.title)
        assertEquals(10.99, firstProduct.price, 0.01)
        assertEquals("Test description 1", firstProduct.description)
        assertEquals("test category", firstProduct.category)
        assertEquals("https://test.com/image1.jpg", firstProduct.image)
        assertEquals(4.5, firstProduct.rating.rate, 0.01)
        assertEquals(100, firstProduct.rating.count)

        val secondProduct = result.data[1]
        assertEquals(2, secondProduct.id)
        assertEquals("Test Product 2", secondProduct.title)
        assertEquals(20.99, secondProduct.price, 0.01)
    }

    @Test
    fun `getProducts returns error when JSON file cannot be read`() = runTest {
        // Given
        every { context.assets.open("product_mock.json") } throws Exception("File not found")

        // When
        val result = mockRepository.getProducts()

        // Then
        assertTrue("Result should be Error", result is DomainResult.Error)
        result as DomainResult.Error
        assertTrue(
            "Error message should contain 'Failed to load mock products'",
            result.message.startsWith("Failed to load mock products:")
        )
    }

    @Test
    fun `getProducts returns empty list when JSON is empty array`() = runTest {
        // Given
        val emptyJsonData = "[]"
        val inputStream = emptyJsonData.byteInputStream()
        every { context.assets.open("product_mock.json") } returns inputStream

        // When
        val result = mockRepository.getProducts()

        // Then
        assertTrue("Result should be Success", result is DomainResult.Success<*>)
        result as DomainResult.Success<List<Product>>
        assertTrue("Should return empty list", result.data.isEmpty())
    }

    @Test
    fun `getProducts handles malformed JSON gracefully`() = runTest {
        // Given
        val malformedJson = "{ invalid json }"
        val inputStream = malformedJson.byteInputStream()
        every { context.assets.open("product_mock.json") } returns inputStream

        // When
        val result = mockRepository.getProducts()

        // Then
        assertTrue("Result should be Error", result is DomainResult.Error)
        result as DomainResult.Error
        assertTrue(
            "Error message should contain 'Failed to load mock products'",
            result.message.startsWith("Failed to load mock products:")
        )
    }
}