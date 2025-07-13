package com.example.feature.product.wiring

import com.example.feature.product.api.data.ProductRepository
import com.example.feature.product.impl.data.ApiProductMapper
import com.example.feature.product.impl.data.repository.ProductRepositoryImpl
import com.example.feature.product.impl.remote.ProductApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ProductProdModule {

    @Provides
    @Singleton
    fun provideProductApi(
        retrofit: Retrofit
    ): ProductApi {
        return retrofit.create(ProductApi::class.java)
    }

    @Provides
    @Singleton
    fun provideProductRepository(
        api: ProductApi,
        mapper: ApiProductMapper
    ): ProductRepository = ProductRepositoryImpl(api, mapper)
}