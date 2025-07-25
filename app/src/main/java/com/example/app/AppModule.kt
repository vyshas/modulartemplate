package com.example.app

import com.example.core.network.GlobalBaseUrl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    @GlobalBaseUrl
    fun provideGlobalBaseUrl(): String = BuildConfig.GLOBAL_BASE_URL
}
