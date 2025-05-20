package com.example.feature.order.impl.domain

import com.example.feature.order.api.domain.model.Order
import com.example.feature.order.api.domain.usecase.GetOrderDetailsUseCase
import com.example.feature.order.impl.data.repository.OrderRepositoryImpl
import javax.inject.Inject

class GetOrderDetailsUseCaseImpl @Inject constructor(
    private val repositoryImpl: OrderRepositoryImpl
) : GetOrderDetailsUseCase {

    override suspend fun getOrder(): Order {
        return repositoryImpl.getOrder()
    }
}