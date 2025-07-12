package com.example.feature.product.impl.data

import com.example.feature.product.api.domain.model.Product
import com.example.feature.product.api.domain.model.Rating
import org.junit.Before
import org.junit.Test
import org.junit.Assert.assertEquals

/**
 * Tests for ApiProductMapper
 *
 * Tests the mapping logic between API models (ApiProduct) and domain models (Product).
 */
class ApiProductMapperTest {

    private lateinit var mapper: ApiProductMapper

    @Before
    fun setup() {
        mapper = ApiProductMapper()
    }

    @Test
    fun `map converts ApiProduct to Product correctly`() {
        // Given
        val apiRating = ApiRating(rate = 4.5, count = 120)
        val apiProduct = ApiProduct(
            id = 1,
            title = "Test Product",
            price = 29.99,
            description = "This is a test product description",
            category = "electronics",
            image = "https://example.com/image.jpg",
            rating = apiRating
        )

        // When
        val result = mapper.map(apiProduct)

        // Then
        assertEquals(1, result.id)
        assertEquals("Test Product", result.title)
        assertEquals(29.99, result.price, 0.01)
        assertEquals("This is a test product description", result.description)
        assertEquals("electronics", result.category)
        assertEquals("https://example.com/image.jpg", result.image)
        assertEquals(4.5, result.rating.rate, 0.01)
        assertEquals(120, result.rating.count)
    }

    @Test
    fun `map handles zero rating correctly`() {
        // Given
        val apiRating = ApiRating(rate = 0.0, count = 0)
        val apiProduct = ApiProduct(
            id = 2,
            title = "No Rating Product",
            price = 15.00,
            description = "Product with no ratings",
            category = "books",
            image = "https://example.com/book.jpg",
            rating = apiRating
        )

        // When
        val result = mapper.map(apiProduct)

        // Then
        assertEquals(2, result.id)
        assertEquals("No Rating Product", result.title)
        assertEquals(15.00, result.price, 0.01)
        assertEquals("Product with no ratings", result.description)
        assertEquals("books", result.category)
        assertEquals("https://example.com/book.jpg", result.image)
        assertEquals(0.0, result.rating.rate, 0.01)
        assertEquals(0, result.rating.count)
    }

    @Test
    fun `map handles high rating values correctly`() {
        // Given
        val apiRating = ApiRating(rate = 5.0, count = 999)
        val apiProduct = ApiProduct(
            id = 3,
            title = "Highly Rated Product",
            price = 99.99,
            description = "A product with maximum rating",
            category = "premium",
            image = "https://example.com/premium.jpg",
            rating = apiRating
        )

        // When
        val result = mapper.map(apiProduct)

        // Then
        assertEquals(3, result.id)
        assertEquals("Highly Rated Product", result.title)
        assertEquals(99.99, result.price, 0.01)
        assertEquals("A product with maximum rating", result.description)
        assertEquals("premium", result.category)
        assertEquals("https://example.com/premium.jpg", result.image)
        assertEquals(5.0, result.rating.rate, 0.01)
        assertEquals(999, result.rating.count)
    }

    @Test
    fun `map handles empty strings correctly`() {
        // Given
        val apiRating = ApiRating(rate = 3.2, count = 45)
        val apiProduct = ApiProduct(
            id = 4,
            title = "",
            price = 0.0,
            description = "",
            category = "",
            image = "",
            rating = apiRating
        )

        // When
        val result = mapper.map(apiProduct)

        // Then
        assertEquals(4, result.id)
        assertEquals("", result.title)
        assertEquals(0.0, result.price, 0.01)
        assertEquals("", result.description)
        assertEquals("", result.category)
        assertEquals("", result.image)
        assertEquals(3.2, result.rating.rate, 0.01)
        assertEquals(45, result.rating.count)
    }

    @Test
    fun `map handles decimal rating values correctly`() {
        // Given
        val apiRating = ApiRating(rate = 4.123456789, count = 1000000)
        val apiProduct = ApiProduct(
            id = 5,
            title = "Precise Rating Product",
            price = 123.456,
            description = "Product with precise decimal values",
            category = "precision",
            image = "https://example.com/precise.jpg",
            rating = apiRating
        )

        // When
        val result = mapper.map(apiProduct)

        // Then
        assertEquals(5, result.id)
        assertEquals("Precise Rating Product", result.title)
        assertEquals(123.456, result.price, 0.001)
        assertEquals("Product with precise decimal values", result.description)
        assertEquals("precision", result.category)
        assertEquals("https://example.com/precise.jpg", result.image)
        assertEquals(4.123456789, result.rating.rate, 0.000000001)
        assertEquals(1000000, result.rating.count)
    }

    @Test
    fun `map preserves all data integrity during conversion`() {
        // Given - Test with realistic e-commerce data
        val apiRating = ApiRating(rate = 4.7, count = 543)
        val apiProduct = ApiProduct(
            id = 123,
            title = "Samsung Galaxy S23 Ultra 5G",
            price = 1199.99,
            description = "Latest Samsung flagship smartphone with advanced camera system and S Pen",
            category = "smartphones",
            image = "https://images.samsung.com/galaxy-s23-ultra.jpg",
            rating = apiRating
        )

        // When
        val domainProduct = mapper.map(apiProduct)

        // Then - Verify complete data integrity
        with(domainProduct) {
            assertEquals("ID should be preserved", apiProduct.id, id)
            assertEquals("Title should be preserved", apiProduct.title, title)
            assertEquals("Price should be preserved", apiProduct.price, price, 0.01)
            assertEquals("Description should be preserved", apiProduct.description, description)
            assertEquals("Category should be preserved", apiProduct.category, category)
            assertEquals("Image should be preserved", apiProduct.image, image)
            assertEquals(
                "Rating rate should be preserved",
                apiProduct.rating.rate,
                rating.rate,
                0.01
            )
            assertEquals("Rating count should be preserved", apiProduct.rating.count, rating.count)
        }

        // Verify the mapped product is the correct type
        assert(domainProduct is Product) { "Result should be Product domain model" }
        assert(domainProduct.rating is Rating) { "Rating should be Rating domain model" }
    }
}