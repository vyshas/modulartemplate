package com.example.feature.recipe.impl.ui.recipelist

import app.cash.turbine.test
import com.example.core.domain.DomainResult
import com.example.feature.recipe.api.domain.usecase.GetRecipesUseCase
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
class RecipeViewModelTest {

    private lateinit var getrecipesUseCase: GetRecipesUseCase
    private lateinit var recipeViewModel: RecipeViewModel
    private val testDispatcher = StandardTestDispatcher()
    private val dispatcherProvider = TestDispatcherProvider(testDispatcher)

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        getrecipesUseCase = mockk()
        recipeViewModel = RecipeViewModel(getrecipesUseCase, dispatcherProvider)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial uiState is Loading`() = runTest(testDispatcher) {
        // Given
        coEvery { getrecipesUseCase.getrecipes() } returns DomainResult.Success(emptyList())

        // When & Then
        recipeViewModel.uiState.test {
            assertTrue(awaitItem() is RecipeUiState.Loading)
            cancelAndConsumeRemainingEvents()
        }
    }
}
