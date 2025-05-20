package com.example.feature.order.api.domain.usecase

import com.example.feature.order.api.domain.model.Order

interface GetOrderDetailsUseCase {
    suspend fun getOrder(): Order
}
