package com.example.feature.order.impl.data.repository

import com.example.feature.order.impl.data.api.OrderApi
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class OrderRepositoryImplTest {

    private lateinit var orderApi: OrderApi
    private lateinit var orderRepository: OrderRepositoryImpl

    @Before
    fun setup() {
        orderApi = mockk()
        orderRepository = OrderRepositoryImpl(orderApi)
    }

    @Test
    fun `getOrder returns Order when API call is successful`() = runBlocking {
        // Given
        val apiResult = Pair(123, 45.67)
        coEvery { orderApi.fetchOrder() } returns apiResult

        // When
        val result = orderRepository.getOrder()

        // Then
        assertEquals(123, result.orderId)
        assertEquals(45.67, result.amount, 0.001)
    }
}