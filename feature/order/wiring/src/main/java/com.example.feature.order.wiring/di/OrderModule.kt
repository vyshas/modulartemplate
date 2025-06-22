package com.example.feature.order.wiring.di

import com.example.core.navigation.BottomNavEntry
import com.example.core.navigation.FeatureEntry
import com.example.feature.order.api.data.OrderRepository
import com.example.feature.order.api.domain.usecase.GetOrderDetailsUseCase
import com.example.feature.order.impl.data.api.OrderApi
import com.example.feature.order.impl.data.repository.OrderRepositoryImpl
import com.example.feature.order.impl.domain.GetOrderDetailsUseCaseImpl
import com.example.feature.order.impl.ui.OrderEntryImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class OrderModule {

    @Binds
    @Singleton
    abstract fun bindGetOrderDetailsUseCase(
        impl: GetOrderDetailsUseCaseImpl
    ): GetOrderDetailsUseCase

    @Binds
    @Singleton
    abstract fun bindOrderRepository(
        impl: OrderRepositoryImpl
    ): OrderRepository

    @Binds
    @IntoSet
    abstract fun bindOrderFeatureEntry(
        orderEntryImpl: OrderEntryImpl
    ): FeatureEntry

    @Binds
    @IntoSet
    abstract fun bindOrderTabEntry(
        home: OrderEntryImpl
    ): BottomNavEntry

    companion object {
        @Provides
        @Singleton
        fun provideOrderApi(): OrderApi = OrderApi()
    }
} 