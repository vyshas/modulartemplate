package com.example.feature.order.impl.ui.orderlist

import app.cash.turbine.test
import com.example.feature.order.api.domain.model.Order
import com.example.feature.order.api.domain.usecase.GetOrderDetailsUseCase
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

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        getOrderDetailsUseCase = mockk()
        orderViewModel = OrderViewModel(getOrderDetailsUseCase)
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
            assertTrue(awaitItem().isLoading)

            // After use case completes
            advanceUntilIdle()
            val successState = awaitItem()
            assertFalse(successState.isLoading)
            assertNotNull(successState.order)
            assertEquals(expectedOrder, successState.order)
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
    fun `onIntent FetchOrder updates uiState`() = runTest(testDispatcher) {
        // Given
        val expectedOrder = Order(123, 45.67)
        coEvery { getOrderDetailsUseCase.getOrder() } returns expectedOrder

        // When & Then
        orderViewModel.uiState.test {
            // Initial loading state
            assertTrue(awaitItem().isLoading)

            // Trigger FetchOrder
            orderViewModel.onIntent(OrderUiIntent.FetchOrder)

            // After use case completes
            advanceUntilIdle()
            val successState = awaitItem()
            assertFalse(successState.isLoading)
            assertNotNull(successState.order)
            assertEquals(expectedOrder, successState.order)
            cancelAndConsumeRemainingEvents()
        }
    }
}
