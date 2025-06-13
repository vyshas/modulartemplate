package com.example.feature.order.wiring.di

import com.example.feature.order.api.domain.usecase.GetOrderDetailsUseCase
import com.example.feature.order.impl.data.api.OrderApi
import com.example.feature.order.impl.data.repository.OrderRepositoryImpl
import com.example.feature.order.impl.domain.GetOrderDetailsUseCaseImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class OrderModule {

    @Binds
    @Singleton
    abstract fun bindGetOrderDetailsUseCase(
        impl: GetOrderDetailsUseCaseImpl
    ): GetOrderDetailsUseCase

    companion object {
        @Provides
        @Singleton
        fun provideOrderApi(): OrderApi = OrderApi()

        @Provides
        @Singleton
        fun provideOrderRepositoryImpl(api: OrderApi): OrderRepositoryImpl =
            OrderRepositoryImpl(api)
    }
} 