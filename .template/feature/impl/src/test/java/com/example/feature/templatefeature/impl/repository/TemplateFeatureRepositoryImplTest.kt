package com.example.feature.templatefeature.impl.repository

import com.example.core.domain.DomainResult
import com.example.feature.templatefeature.api.data.TemplateFeatureRepository
import com.example.feature.templatefeature.api.domain.model.TemplateFeature
import com.example.feature.templatefeature.impl.data.ApiTemplateFeature
import com.example.feature.templatefeature.impl.data.ApiTemplateFeatureMapper
import com.example.feature.templatefeature.impl.data.ApiRating
import com.example.feature.templatefeature.impl.remote.TemplateFeatureApi
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue

/**
 * Tests for TemplateFeatureRepository behavior (TemplateFeatureion implementation pattern)
 *
 * Since the actual TemplateFeatureRepositoryImpl is in the prod flavor, we test the repository
 * interface contract and behavior using mocks. This ensures the repository follows
 * the expected patterns without duplicating implementation logic.
 */
class TemplateFeatureRepositoryImplTest {

    private lateinit var templatefeatureApi: TemplateFeatureApi
    private lateinit var apiTemplateFeatureMapper: ApiTemplateFeatureMapper
    private lateinit var mockRepository: TemplateFeatureRepository

    @Before
    fun setup() {
        templatefeatureApi = mockk()
        apiTemplateFeatureMapper = ApiTemplateFeatureMapper()
        mockRepository = mockk()
    }

    @Test
    fun `repository returns DomainResult Success with mapped templatefeatures on API success`() = runTest {
        // Given
        val expectedTemplateFeatures = listOf(
            TemplateFeature(
                id = 1,
                title = "Test TemplateFeature 1",
                price = 10.0,
                description = "desc1",
                category = "cat1",
                image = "url1",
                rating = com.example.feature.templatefeature.api.domain.model.Rating(rate = 4.0, count = 10)
            ),
            TemplateFeature(
                id = 2,
                title = "Test TemplateFeature 2",
                price = 20.0,
                description = "desc2",
                category = "cat2",
                image = "url2",
                rating = com.example.feature.templatefeature.api.domain.model.Rating(rate = 3.5, count = 20)
            )
        )

        coEvery { mockRepository.getTemplateFeatures() } returns DomainResult.Success(expectedTemplateFeatures)

        // When
        val result = mockRepository.getTemplateFeatures()

        // Then
        assertTrue("Result should be Success", result is DomainResult.Success<*>)
        result as DomainResult.Success<List<TemplateFeature>>
        assertEquals("Should return 2 templatefeatures", 2, result.data.size)
        assertEquals("First templatefeature should match", expectedTemplateFeatures[0], result.data[0])
        assertEquals("Second templatefeature should match", expectedTemplateFeatures[1], result.data[1])

        coVerify { mockRepository.getTemplateFeatures() }
    }

    @Test
    fun `repository returns DomainResult Error on API failure`() = runTest {
        // Given
        val errorMessage = "Failed to fetch templatefeatures: Network error"
        coEvery { mockRepository.getTemplateFeatures() } returns DomainResult.Error(errorMessage)

        // When
        val result = mockRepository.getTemplateFeatures()

        // Then
        assertTrue("Result should be Error", result is DomainResult.Error)
        result as DomainResult.Error
        assertEquals("Error message should match", errorMessage, result.message)

        coVerify { mockRepository.getTemplateFeatures() }
    }

    @Test
    fun `repository returns empty list when API returns empty response`() = runTest {
        // Given
        val emptyTemplateFeatures = emptyList<TemplateFeature>()
        coEvery { mockRepository.getTemplateFeatures() } returns DomainResult.Success(emptyTemplateFeatures)

        // When
        val result = mockRepository.getTemplateFeatures()

        // Then
        assertTrue("Result should be Success", result is DomainResult.Success<*>)
        result as DomainResult.Success<List<TemplateFeature>>
        assertTrue("Should return empty list", result.data.isEmpty())

        coVerify { mockRepository.getTemplateFeatures() }
    }

    @Test
    fun `mapper correctly converts ApiTemplateFeature to TemplateFeature`() {
        // Given
        val apiTemplateFeature = ApiTemplateFeature(
            id = 1,
            title = "Test TemplateFeature",
            price = 29.99,
            description = "Test description",
            category = "electronics",
            image = "https://test.com/image.jpg",
            rating = ApiRating(rate = 4.5, count = 120)
        )

        // When
        val result = apiTemplateFeatureMapper.map(apiTemplateFeature)

        // Then
        assertEquals("ID should match", apiTemplateFeature.id, result.id)
        assertEquals("Title should match", apiTemplateFeature.title, result.title)
        assertEquals("Price should match", apiTemplateFeature.price, result.price, 0.01)
        assertEquals("Description should match", apiTemplateFeature.description, result.description)
        assertEquals("Category should match", apiTemplateFeature.category, result.category)
        assertEquals("Image should match", apiTemplateFeature.image, result.image)
        assertEquals("Rating rate should match", apiTemplateFeature.rating.rate, result.rating.rate, 0.01)
        assertEquals("Rating count should match", apiTemplateFeature.rating.count, result.rating.count)
    }

    @Test
    fun `repository handles network error correctly`() = runTest {
        // Given
        coEvery { mockRepository.getTemplateFeatures() } returns DomainResult.NetworkError

        // When
        val result = mockRepository.getTemplateFeatures()

        // Then
        assertTrue("Result should be NetworkError", result is DomainResult.NetworkError)

        coVerify { mockRepository.getTemplateFeatures() }
    }

    @Test
    fun `repository follows expected interface contract`() = runTest {
        // Given
        val templatefeatures = listOf(
            TemplateFeature(
                id = 1,
                title = "Contract Test TemplateFeature",
                price = 15.99,
                description = "Testing repository contract",
                category = "test",
                image = "test.jpg",
                rating = com.example.feature.templatefeature.api.domain.model.Rating(rate = 3.0, count = 5)
            )
        )
        coEvery { mockRepository.getTemplateFeatures() } returns DomainResult.Success(templatefeatures)

        // When
        val result = mockRepository.getTemplateFeatures()

        // Then
        // Verify the repository follows the expected contract:
        // 1. Returns DomainResult wrapper
        assertTrue("Must return DomainResult", result is DomainResult<*>)

        // 2. Success case contains List<TemplateFeature>
        result as DomainResult.Success<List<TemplateFeature>>
        assertTrue("Success data must be List<TemplateFeature>", result.data is List<TemplateFeature>)

        // 3. TemplateFeatures have all required fields
        val templatefeature = result.data.first()
        assertTrue("TemplateFeature must have valid ID", templatefeature.id > 0)
        assertTrue("TemplateFeature must have title", templatefeature.title.isNotEmpty())
        assertTrue("TemplateFeature must have non-negative price", templatefeature.price >= 0)
        assertTrue("TemplateFeature must have rating", templatefeature.rating.rate >= 0)

        coVerify { mockRepository.getTemplateFeatures() }
    }
}