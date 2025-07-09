package com.example.feature.order.impl.domain

import com.example.feature.order.api.data.OrderRepository
import com.example.feature.order.api.domain.model.Order
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class GetOrderDetailsUseCaseImplTest {

    private lateinit var orderRepository: OrderRepository
    private lateinit var getOrderDetailsUseCase: GetOrderDetailsUseCaseImpl

    @Before
    fun setup() {
        orderRepository = mockk()
        getOrderDetailsUseCase = GetOrderDetailsUseCaseImpl(orderRepository)
    }

    @Test
    fun `getOrder calls repository and returns its result`() = runBlocking {
        // Given
        val expectedOrder = Order(123, 45.67)
        coEvery { orderRepository.getOrder() } returns expectedOrder

        // When
        val result = getOrderDetailsUseCase.getOrder()

        // Then
        assertEquals(expectedOrder, result)
        coVerify(exactly = 1) { orderRepository.getOrder() }
    }
}