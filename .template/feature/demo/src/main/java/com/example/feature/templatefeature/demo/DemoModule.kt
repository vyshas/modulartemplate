package com.example.feature.templatefeature.demo

import com.example.core.network.GlobalBaseUrl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DemoModule {
    @Provides
    @Singleton
    @GlobalBaseUrl
    fun provideGlobalBaseUrl(): String = BuildConfig.GLOBAL_BASE_URL
}