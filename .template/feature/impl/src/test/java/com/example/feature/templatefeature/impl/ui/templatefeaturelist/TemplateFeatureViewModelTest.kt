package com.example.feature.templatefeature.impl.ui.templatefeaturelist

import app.cash.turbine.test
import com.example.core.domain.DomainResult
import com.example.feature.templatefeature.api.domain.usecase.GetTemplateFeaturesUseCase
import com.example.testutils.TestDispatcherProvider
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
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
}
