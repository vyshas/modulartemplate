package com.example.feature.templatefeature.impl.domain.usecase

import com.example.core.domain.DomainResult
import com.example.feature.templatefeature.api.data.TemplateFeatureRepository
import com.example.feature.templatefeature.api.domain.model.TemplateFeature
import com.example.feature.templatefeature.api.domain.model.Rating
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue

class GetTemplateFeaturesUseCaseImplTest {

    private lateinit var templatefeatureRepository: TemplateFeatureRepository
    private lateinit var getTemplateFeaturesUseCase: GetTemplateFeaturesUseCaseImpl

    @Before
    fun setup() {
        templatefeatureRepository = mockk()
        getTemplateFeaturesUseCase = GetTemplateFeaturesUseCaseImpl(templatefeatureRepository)
    }

    @Test
    fun `getTemplateFeatures returns templatefeatures from repository on success`() = runTest {
        // Given
        val expectedTemplateFeatures = listOf(
            TemplateFeature(id = 1, title = "TemplateFeature A", price = 10.0, description = "descA", category = "catA", image = "urlA", rating = Rating(rate = 4.0, count = 10)),
            TemplateFeature(id = 2, title = "TemplateFeature B", price = 20.0, description = "descB", category = "catB", image = "urlB", rating = Rating(rate = 3.5, count = 20))
        )
        coEvery { templatefeatureRepository.getTemplateFeatures() } returns DomainResult.Success(expectedTemplateFeatures)

        // When
        val result = getTemplateFeaturesUseCase.getTemplateFeatures()

        // Then
        assertTrue(result is DomainResult.Success)
        result as DomainResult.Success
        assertEquals(expectedTemplateFeatures, result.data)
    }

    @Test
    fun `getTemplateFeatures returns error when repository returns error`() = runTest {
        // Given
        val errorMessage = "Repository Error"
        coEvery { templatefeatureRepository.getTemplateFeatures() } returns DomainResult.Error(errorMessage)

        // When
        val result = getTemplateFeaturesUseCase.getTemplateFeatures()

        // Then
        assertTrue(result is DomainResult.Error)
        result as DomainResult.Error
        assertEquals(errorMessage, result.message)
    }
}