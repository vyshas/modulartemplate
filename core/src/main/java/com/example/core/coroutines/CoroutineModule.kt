package com.example.core.coroutines

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class CoroutineModule {

    @Binds
    abstract fun bindCoroutineDispatchers(
        defaultCoroutineDispatchers: DefaultCoroutineDispatchers,
    ): CoroutineDispatchers
}
