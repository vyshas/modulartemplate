package com.example.feature.order.impl.data.repository

import com.example.feature.order.api.data.OrderRepository
import com.example.feature.order.api.domain.model.Order
import com.example.feature.order.impl.data.api.OrderApi
import javax.inject.Inject

class OrderRepositoryImpl @Inject constructor(
    private val api: OrderApi,
) : OrderRepository {

    override suspend fun getOrder(): Order {
        val data = api.fetchOrder()
        return Order(data.first, data.second)
    }
}
