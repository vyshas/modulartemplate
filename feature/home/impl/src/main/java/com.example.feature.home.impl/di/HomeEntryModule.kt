package com.example.feature.home.impl.di

import com.example.feature.home.api.HomeEntry
import com.example.feature.home.impl.ui.screen.HomeEntryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class HomeEntryModule {

    @Binds
    abstract fun bindHomeEntry(
        impl: HomeEntryImpl
    ): HomeEntry
}