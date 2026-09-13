package com.example.feature.home.impl.ui.homedetail

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import com.example.core.domain.DomainResult
import com.example.feature.home.api.domain.model.HomeItem
import com.example.feature.home.api.domain.usecase.GetHomeItemByIdUseCase
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
class HomeDetailViewModelTest {

    private lateinit var getHomeItemByIdUseCase: GetHomeItemByIdUseCase
    private val testDispatcher = StandardTestDispatcher()
    private val dispatcherProvider = TestDispatcherProvider(testDispatcher)
    private val itemId = 123

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        getHomeItemByIdUseCase = mockk()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    private fun createViewModel(): HomeDetailViewModel {
        val savedStateHandle = SavedStateHandle(mapOf(HomeDetailViewModel.KEY_ITEM_ID to itemId))
        return HomeDetailViewModel(savedStateHandle, getHomeItemByIdUseCase, dispatcherProvider)
    }

    @Test
    fun `initial uiState is Loading`() = runTest(testDispatcher) {
        // Given
        coEvery { getHomeItemByIdUseCase(itemId) } returns DomainResult.Success(HomeItem(itemId, "Test"))
        val viewModel = createViewModel()

        // When & Then
        viewModel.uiState.test {
            assertTrue(awaitItem() is HomeDetailUiState.Loading)
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `uiState transitions to Success when use case returns success`() = runTest(testDispatcher) {
        // Given
        val item = HomeItem(id = itemId, title = "Test Item")
        coEvery { getHomeItemByIdUseCase(itemId) } returns DomainResult.Success(item)
        val viewModel = createViewModel()

        // When & Then
        viewModel.uiState.test {
            assertTrue(awaitItem() is HomeDetailUiState.Loading) // Initial loading state
            advanceUntilIdle() // Allow coroutines to complete
            val successState = awaitItem()
            assertTrue(successState is HomeDetailUiState.Success)
            assertEquals(item, (successState as HomeDetailUiState.Success).item)
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `uiState transitions to Error and emits uiEffect when use case returns error`() = runTest(testDispatcher) {
        // Given
        val errorMessage = "Failed to fetch"
        coEvery { getHomeItemByIdUseCase(itemId) } returns DomainResult.Error(errorMessage)
        val viewModel = createViewModel()

        // When & Then
        viewModel.uiEffect.test {
            viewModel.uiState.test {
                assertTrue(awaitItem() is HomeDetailUiState.Loading) // Initial loading state
                advanceUntilIdle()
                val errorState = awaitItem()
                assertTrue(errorState is HomeDetailUiState.Error)
                assertEquals(errorMessage, (errorState as HomeDetailUiState.Error).message)
                cancelAndConsumeRemainingEvents()
            }
            
            // Check effect
            val effect = awaitItem()
            assertTrue(effect is HomeDetailUiEffect.ShowToast)
            assertEquals(errorMessage, (effect as HomeDetailUiEffect.ShowToast).message)
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `retry triggers new fetch of home item`() = runTest(testDispatcher) {
        // Given
        val item1 = HomeItem(id = itemId, title = "Item 1")
        val item2 = HomeItem(id = itemId, title = "Item 2")

        coEvery { getHomeItemByIdUseCase(itemId) } returnsMany listOf(
            DomainResult.Success(item1),
            DomainResult.Success(item2),
        )

        val viewModel = createViewModel()

        viewModel.uiState.test {
            // Initial fetch
            assertTrue(awaitItem() is HomeDetailUiState.Loading)
            advanceUntilIdle()
            assertEquals(item1, (awaitItem() as HomeDetailUiState.Success).item)

            // Trigger retry
            viewModel.retry()
            advanceUntilIdle()

            // Then
            assertTrue(awaitItem() is HomeDetailUiState.Loading) // Loading again after retry
            assertEquals(item2, (awaitItem() as HomeDetailUiState.Success).item)
            cancelAndConsumeRemainingEvents()
        }
    }
}
