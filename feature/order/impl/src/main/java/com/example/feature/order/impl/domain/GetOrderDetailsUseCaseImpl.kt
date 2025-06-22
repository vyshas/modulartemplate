package com.example.feature.order.impl.domain

import com.example.feature.order.api.data.OrderRepository
import com.example.feature.order.api.domain.model.Order
import com.example.feature.order.api.domain.usecase.GetOrderDetailsUseCase
import javax.inject.Inject

class GetOrderDetailsUseCaseImpl @Inject constructor(
    private val orderRepository: OrderRepository
) : GetOrderDetailsUseCase {

    override suspend fun getOrder(): Order {
        return orderRepository.getOrder()
    }
}