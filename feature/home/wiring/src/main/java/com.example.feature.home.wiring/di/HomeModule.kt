package com.example.feature.home.wiring.di

import com.example.core.navigation.BottomNavEntry
import com.example.core.navigation.FeatureEntry
import com.example.feature.home.api.HomeEntry
import com.example.feature.home.api.HomeNavigator
import com.example.feature.home.api.data.HomeRepository
import com.example.feature.home.api.domain.usecase.GetHomeItemByIdUseCase
import com.example.feature.home.api.domain.usecase.GetHomeItemsUseCase
import com.example.feature.home.impl.data.api.HomeApi
import com.example.feature.home.impl.data.repository.HomeRepositoryImpl
import com.example.feature.home.impl.domain.GetHomeItemByIdUseCaseImpl
import com.example.feature.home.impl.domain.GetHomeItemsUseCaseImpl
import com.example.feature.home.impl.navigation.HomeNavigatorImpl
import com.example.feature.home.impl.ui.HomeEntryImpl
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
    @Singleton
    abstract fun bindHomeRepository(
        impl: HomeRepositoryImpl
    ): HomeRepository

    @Binds
    @Singleton
    abstract fun bindGetHomeItemByIdUseCase(
        impl: GetHomeItemByIdUseCaseImpl
    ): GetHomeItemByIdUseCase

    @Binds
    @IntoSet
    abstract fun bindHomeEntry(
        homeEntryImpl: HomeEntryImpl
    ): FeatureEntry

    @Binds
    @IntoSet
    abstract fun bindHomeTabEntry(
        home: HomeEntryImpl
    ): BottomNavEntry

    @Binds
    @Singleton
    abstract fun bindHomeNavigator(
        impl: HomeNavigatorImpl
    ): HomeNavigator

    @Binds
    @Singleton
    abstract fun bindHomeEntryAsHomeEntry(
        impl: HomeEntryImpl
    ): HomeEntry

    companion object {
        @Provides
        @Singleton
        fun provideHomeApi(): HomeApi = HomeApi()
    }
}