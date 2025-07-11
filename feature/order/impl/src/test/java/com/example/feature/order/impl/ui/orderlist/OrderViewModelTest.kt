package com.example.feature.order.impl.ui.orderlist

import app.cash.turbine.test
import com.example.feature.order.api.domain.model.Order
import com.example.feature.order.api.domain.usecase.GetOrderDetailsUseCase
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
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class OrderViewModelTest {

    private lateinit var getOrderDetailsUseCase: GetOrderDetailsUseCase
    private lateinit var orderViewModel: OrderViewModel
    private val testDispatcher = StandardTestDispatcher()
    private val dispatcherProvider = TestDispatcherProvider(testDispatcher)

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        getOrderDetailsUseCase = mockk()
        orderViewModel = OrderViewModel(getOrderDetailsUseCase, dispatcherProvider)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial uiState is Loading`() = runTest(testDispatcher) {
        // Given
        coEvery { getOrderDetailsUseCase.getOrder() } returns Order(1, 100.0)

        // When & Then
        orderViewModel.uiState.test {
            val initialState = awaitItem()
            assertTrue(initialState.isLoading)
            assertNull(initialState.order)
            assertNull(initialState.error)
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `uiState transitions to Success with order data`() = runTest(testDispatcher) {
        // Given
        val expectedOrder = Order(123, 45.67)
        coEvery { getOrderDetailsUseCase.getOrder() } returns expectedOrder

        // When & Then
        orderViewModel.uiState.test {
            // Initial loading state
            val loadingState = awaitItem()
            assertTrue(loadingState.isLoading)
            assertNull(loadingState.order)
            assertNull(loadingState.error)

            // After use case completes
            advanceUntilIdle()
            val successState = awaitItem()
            assertFalse(successState.isLoading)
            assertNotNull(successState.order)
            assertEquals(expectedOrder, successState.order)
            assertNull(successState.error)
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `uiState transitions to Error when use case throws exception`() = runTest(testDispatcher) {
        // Given
        val errorMessage = "Failed to get order"
        coEvery { getOrderDetailsUseCase.getOrder() } throws Exception(errorMessage)

        // When & Then
        orderViewModel.uiState.test {
            // Initial loading state
            val loadingState = awaitItem()
            assertTrue(loadingState.isLoading)
            assertNull(loadingState.order)
            assertNull(loadingState.error)

            // After use case completes with error
            advanceUntilIdle()
            val errorState = awaitItem()
            assertFalse(errorState.isLoading)
            assertNull(errorState.order)
            assertEquals(errorMessage, errorState.error)
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `onIntent OnViewHomeDetailClicked emits NavigateToHomeDetail effect`() =
        runTest(testDispatcher) {
            // Given
            val orderId = 123
            val order = Order(orderId, 100.0)
            coEvery { getOrderDetailsUseCase.getOrder() } returns order

            // Ensure the order is loaded into uiState first
            orderViewModel.uiState.test {
                awaitItem() // Loading
                advanceUntilIdle()
                awaitItem() // Success with order
                cancelAndConsumeRemainingEvents()
            }

            // When & Then
            orderViewModel.uiEffect.test {
                orderViewModel.onIntent(OrderUiIntent.OnViewHomeDetailClicked)
                val effect = awaitItem()
                assertTrue(effect is OrderUiEffect.NavigateToHomeDetail)
                val emittedOrderId = (effect as OrderUiEffect.NavigateToHomeDetail).homeId
                assertEquals(orderId, emittedOrderId)
                cancelAndConsumeRemainingEvents()
            }
        }

    @Test
    fun `onIntent FetchOrder triggers refresh`() = runTest(testDispatcher) {
        // Given
        val order1 = Order(123, 45.67)
        val order2 = Order(456, 78.90)
        coEvery { getOrderDetailsUseCase.getOrder() } returnsMany listOf(order1, order2)

        // When & Then
        orderViewModel.uiState.test {
            // Initial fetch
            assertTrue(awaitItem().isLoading)
            advanceUntilIdle()
            assertEquals(order1, awaitItem().order)

            // Trigger refresh
            orderViewModel.onIntent(OrderUiIntent.FetchOrder)
            advanceUntilIdle()

            // Then
            assertTrue(awaitItem().isLoading) // Loading again after refresh
            assertEquals(order2, awaitItem().order)
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `onIntent OnViewHomeDetailClicked does nothing when order is null`() =
        runTest(testDispatcher) {
            // Given
            coEvery { getOrderDetailsUseCase.getOrder() } throws Exception("No order")

            // Ensure we're in error state with no order
            orderViewModel.uiState.test {
                awaitItem() // Loading
            advanceUntilIdle()
            val errorState = awaitItem()
            assertNull(errorState.order)
            cancelAndConsumeRemainingEvents()
        }

        // When & Then
        orderViewModel.uiEffect.test {
            orderViewModel.onIntent(OrderUiIntent.OnViewHomeDetailClicked)
            // No effect should be emitted since order is null
            expectNoEvents()
        }
    }
}