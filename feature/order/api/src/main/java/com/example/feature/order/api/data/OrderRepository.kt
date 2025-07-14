package com.example.feature.order.api.data

import com.example.feature.order.api.domain.model.Order

interface OrderRepository {
    suspend fun getOrder(): Order
}
