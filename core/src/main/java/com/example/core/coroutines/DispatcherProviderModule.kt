package com.example.core.coroutines

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DispatcherProviderModule {
    @Binds
    @Singleton
    abstract fun bindDispatcherProvider(
        impl: StandardDispatcherProvider
    ): DispatcherProvider
}
