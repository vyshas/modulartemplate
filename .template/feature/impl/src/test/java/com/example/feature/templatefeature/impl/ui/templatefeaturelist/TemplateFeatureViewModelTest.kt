package com.example.feature.templatefeature.impl.ui.templatefeaturelist

import app.cash.turbine.test
import com.example.core.domain.DomainResult
import com.example.feature.templatefeature.api.domain.model.TemplateFeature
import com.example.feature.templatefeature.api.domain.model.Rating
import com.example.feature.templatefeature.api.domain.usecase.GetTemplateFeaturesUseCase
import com.example.testutils.TestDispatcherProvider
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class TemplateFeatureViewModelTest {

    private lateinit var getTemplateFeaturesUseCase: GetTemplateFeaturesUseCase
    private lateinit var templatefeatureViewModel: TemplateFeatureViewModel
    private val testDispatcher = StandardTestDispatcher()
    private val dispatcherProvider = TestDispatcherProvider(testDispatcher)

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        getTemplateFeaturesUseCase = mockk()
        templatefeatureViewModel = TemplateFeatureViewModel(getTemplateFeaturesUseCase, dispatcherProvider)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial uiState is Loading`() = runTest(testDispatcher) {
        // Given
        coEvery { getTemplateFeaturesUseCase.getTemplateFeatures() } returns DomainResult.Success(emptyList())

        // When & Then
        templatefeatureViewModel.uiState.test {
            assertTrue(awaitItem() is TemplateFeatureUiState.Loading)
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `uiState transitions to Success when use case returns success`() = runTest(testDispatcher) {
        // Given
        val templatefeatures = listOf(
            TemplateFeature(
                id = 1,
                title = "Test TemplateFeature",
                price = 29.99,
                description = "Test description",
                category = "Test category",
                image = "test.jpg",
                rating = Rating(4.5, 10)
            )
        )
        coEvery { getTemplateFeaturesUseCase.getTemplateFeatures() } returns DomainResult.Success(templatefeatures)

        // When & Then
        templatefeatureViewModel.uiState.test {
            assertTrue(awaitItem() is TemplateFeatureUiState.Loading) // Initial loading state
            advanceUntilIdle() // Allow coroutines to complete
            val successState = awaitItem()
            assertTrue(successState is TemplateFeatureUiState.Success)
            assertEquals(templatefeatures, (successState as TemplateFeatureUiState.Success).templatefeatures)
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `uiState transitions to Error when use case returns error`() = runTest(testDispatcher) {
        // Given
        val errorMessage = "Failed to fetch templatefeatures"
        coEvery { getTemplateFeaturesUseCase.getTemplateFeatures() } returns DomainResult.Error(errorMessage)

        // When & Then
        templatefeatureViewModel.uiState.test {
            assertTrue(awaitItem() is TemplateFeatureUiState.Loading) // Initial loading state
            advanceUntilIdle()
            val errorState = awaitItem()
            assertTrue(errorState is TemplateFeatureUiState.Error)
            assertEquals(errorMessage, (errorState as TemplateFeatureUiState.Error).message)
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `uiState transitions to Error when use case returns network error`() =
        runTest(testDispatcher) {
            // Given
            coEvery { getTemplateFeaturesUseCase.getTemplateFeatures() } returns DomainResult.NetworkError

            // When & Then
            templatefeatureViewModel.uiState.test {
                assertTrue(awaitItem() is TemplateFeatureUiState.Loading) // Initial loading state
                advanceUntilIdle()
                val errorState = awaitItem()
                assertTrue(errorState is TemplateFeatureUiState.Error)
                assertEquals("Network error", (errorState as TemplateFeatureUiState.Error).message)
                cancelAndConsumeRemainingEvents()
            }
        }

    @Test
    fun `FetchTemplateFeatures intent triggers refresh`() = runTest(testDispatcher) {
        // Given
        val templatefeatures1 = listOf(
            TemplateFeature(1, "TemplateFeature 1", 10.0, "Desc 1", "Cat 1", "img1.jpg", Rating(4.0, 5))
        )
        val templatefeatures2 = listOf(
            TemplateFeature(2, "TemplateFeature 2", 20.0, "Desc 2", "Cat 2", "img2.jpg", Rating(4.5, 10))
        )

        coEvery { getTemplateFeaturesUseCase.getTemplateFeatures() } returnsMany listOf(
            DomainResult.Success(templatefeatures1),
            DomainResult.Success(templatefeatures2)
        )

        templatefeatureViewModel.uiState.test {
            // Initial fetch
            assertTrue(awaitItem() is TemplateFeatureUiState.Loading)
            advanceUntilIdle()
            assertEquals(templatefeatures1, (awaitItem() as TemplateFeatureUiState.Success).templatefeatures)

            // Trigger refresh
            templatefeatureViewModel.onIntent(TemplateFeatureUiIntent.FetchTemplateFeatures)
            advanceUntilIdle()

            // Then
            assertTrue(awaitItem() is TemplateFeatureUiState.Loading) // Loading again after refresh
            assertEquals(templatefeatures2, (awaitItem() as TemplateFeatureUiState.Success).templatefeatures)
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `TemplateFeatureClicked intent emits NavigateToTemplateFeatureDetail effect`() = runTest(testDispatcher) {
        // Given
        val templatefeatureId = 123
        coEvery { getTemplateFeaturesUseCase.getTemplateFeatures() } returns DomainResult.Success(emptyList())

        // When & Then
        templatefeatureViewModel.uiEffect.test {
            templatefeatureViewModel.onIntent(TemplateFeatureUiIntent.TemplateFeatureClicked(templatefeatureId))
            advanceUntilIdle()

            val effect = awaitItem()
            assertTrue(effect is TemplateFeatureUiEffect.NavigateToTemplateFeatureDetail)
            assertEquals(templatefeatureId, (effect as TemplateFeatureUiEffect.NavigateToTemplateFeatureDetail).templatefeatureId)
            cancelAndConsumeRemainingEvents()
        }
    }
}