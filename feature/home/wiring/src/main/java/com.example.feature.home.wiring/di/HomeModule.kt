package com.example.feature.home.wiring.di

import com.example.core.navigation.FeatureEntry
import com.example.feature.home.api.domain.usecase.GetHomeItemsUseCase
import com.example.feature.home.impl.data.api.HomeApi
import com.example.feature.home.impl.data.repository.HomeRepositoryImpl
import com.example.feature.home.impl.domain.GetHomeItemsUseCaseImpl
import com.example.feature.home.impl.ui.screen.HomeEntryImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class HomeModule {

    @Binds
    @Singleton
    abstract fun bindGetHomeItemsUseCase(
        impl: GetHomeItemsUseCaseImpl
    ): GetHomeItemsUseCase

    @Binds
    @IntoSet
    abstract fun bindHomeEntry(homeEntryImpl: HomeEntryImpl): FeatureEntry

    companion object {
        @Provides
        @Singleton
        fun provideHomeApi(): HomeApi = HomeApi()

        @Provides
        @Singleton
        fun provideHomeRepositoryImpl(
            api: HomeApi
        ): HomeRepositoryImpl = HomeRepositoryImpl(api)
    }
} 