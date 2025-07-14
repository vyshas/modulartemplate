package com.example.feature.home.impl.ui.homelist

import app.cash.turbine.test
import com.example.core.domain.DomainResult
import com.example.feature.home.api.domain.model.HomeItem
import com.example.feature.home.api.domain.usecase.GetHomeItemsUseCase
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
class HomeViewModelTest {

    private lateinit var getHomeItemsUseCase: GetHomeItemsUseCase
    private lateinit var homeViewModel: HomeViewModel
    private val testDispatcher = StandardTestDispatcher()
    private val dispatcherProvider = TestDispatcherProvider(testDispatcher)

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        getHomeItemsUseCase = mockk()
        homeViewModel = HomeViewModel(getHomeItemsUseCase, dispatcherProvider)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial uiState is Loading`() = runTest(testDispatcher) {
        // Given
        coEvery { getHomeItemsUseCase.getHomeItems() } returns DomainResult.Success(emptyList())

        // When & Then
        homeViewModel.uiState.test {
            assertTrue(awaitItem() is HomeUiState.Loading)
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `uiState transitions to Success when use case returns success`() = runTest(testDispatcher) {
        // Given
        val homeItems = listOf(HomeItem(id = 1, title = "Test Item"))
        coEvery { getHomeItemsUseCase.getHomeItems() } returns DomainResult.Success(homeItems)

        // When & Then
        homeViewModel.uiState.test {
            assertTrue(awaitItem() is HomeUiState.Loading) // Initial loading state
            advanceUntilIdle() // Allow coroutines to complete
            val successState = awaitItem()
            assertTrue(successState is HomeUiState.Success)
            assertEquals(homeItems, (successState as HomeUiState.Success).items)
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `uiState transitions to Error when use case returns error`() = runTest(testDispatcher) {
        // Given
        val errorMessage = "Failed to fetch"
        coEvery { getHomeItemsUseCase.getHomeItems() } returns DomainResult.Error(errorMessage)

        // When & Then
        homeViewModel.uiState.test {
            assertTrue(awaitItem() is HomeUiState.Loading) // Initial loading state
            advanceUntilIdle()
            val errorState = awaitItem()
            assertTrue(errorState is HomeUiState.Error)
            assertEquals(errorMessage, (errorState as HomeUiState.Error).message)
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `uiState transitions to Error when use case returns network error`() = runTest(testDispatcher) {
        // Given
        coEvery { getHomeItemsUseCase.getHomeItems() } returns DomainResult.NetworkError

        // When & Then
        homeViewModel.uiState.test {
            assertTrue(awaitItem() is HomeUiState.Loading) // Initial loading state
            advanceUntilIdle()
            val errorState = awaitItem()
            assertTrue(errorState is HomeUiState.Error)
            assertEquals("Network error", (errorState as HomeUiState.Error).message)
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `refresh triggers new fetch of home items`() = runTest(testDispatcher) {
        // Given
        val homeItems1 = listOf(HomeItem(id = 1, title = "Item 1"))
        val homeItems2 = listOf(HomeItem(id = 2, title = "Item 2"))

        coEvery { getHomeItemsUseCase.getHomeItems() } returnsMany listOf(
            DomainResult.Success(homeItems1),
            DomainResult.Success(homeItems2),
        )

        homeViewModel.uiState.test {
            // Initial fetch
            assertTrue(awaitItem() is HomeUiState.Loading)
            advanceUntilIdle()
            assertEquals(homeItems1, (awaitItem() as HomeUiState.Success).items)

            // Trigger refresh
            homeViewModel.refresh()
            advanceUntilIdle()

            // Then
            assertTrue(awaitItem() is HomeUiState.Loading) // Loading again after refresh
            assertEquals(homeItems2, (awaitItem() as HomeUiState.Success).items)
            cancelAndConsumeRemainingEvents()
        }
    }
}
