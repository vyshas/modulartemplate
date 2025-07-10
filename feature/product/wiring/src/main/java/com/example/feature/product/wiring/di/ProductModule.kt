package com.example.feature.product.wiring.di

import com.example.core.navigation.FeatureEntry
import com.example.feature.product.api.ProductEntry
import com.example.feature.product.api.ProductNavigator
import com.example.feature.product.api.data.ProductRepository
import com.example.feature.product.api.domain.usecase.GetProductsUseCase
import com.example.feature.product.impl.ProductEntryImpl
import com.example.feature.product.impl.navigation.ProductNavigatorImpl
import com.example.feature.product.impl.domain.usecase.GetProductsUseCaseImpl
import com.example.feature.product.impl.data.ApiProductMapper
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class ProductModule {

    @Binds
    @Singleton
    abstract fun bindGetProductsUseCase(
        impl: GetProductsUseCaseImpl
    ): GetProductsUseCase

    @Binds
    @IntoSet
    abstract fun bindProductEntry(
        productEntryImpl: ProductEntryImpl
    ): FeatureEntry

    @Binds
    @Singleton
    abstract fun bindProductNavigator(
        impl: ProductNavigatorImpl
    ): ProductNavigator

    @Binds
    @Singleton
    abstract fun bindProductEntryAsProductEntry(
        impl: ProductEntryImpl
    ): ProductEntry

    companion object {
        @Provides
        @Singleton
        fun provideApiProductMapper(): ApiProductMapper = ApiProductMapper()
    }
}
