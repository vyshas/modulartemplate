package com.example.feature.product.wiring

import android.content.Context
import com.example.feature.product.api.data.ProductRepository
import com.example.feature.product.impl.data.ApiProductMapper
import com.example.feature.product.impl.repository.ProductRepositoryMockImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ProductMockModule {

    @Provides
    @Singleton
    fun provideProductRepository(
        @ApplicationContext context: Context,
        mapper: ApiProductMapper
    ): ProductRepository {
        // Ensure we use application context, which is properly injected by Hilt
        return ProductRepositoryMockImpl(context.applicationContext, mapper)
    }
}