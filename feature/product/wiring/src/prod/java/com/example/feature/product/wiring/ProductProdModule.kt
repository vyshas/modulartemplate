package com.example.feature.product.wiring

import com.example.feature.product.api.data.ProductRepository
import com.example.feature.product.impl.data.ApiProductMapper
import com.example.feature.product.impl.remote.ProductApi
import com.example.feature.product.impl.repository.ProductRepositoryImpl
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
    fun provideProductApi(retrofit: Retrofit): ProductApi = retrofit.create(ProductApi::class.java)

    @Provides
    @Singleton
    fun provideProductRepository(
        api: ProductApi,
        mapper: ApiProductMapper
    ): ProductRepository = ProductRepositoryImpl(api, mapper)
}