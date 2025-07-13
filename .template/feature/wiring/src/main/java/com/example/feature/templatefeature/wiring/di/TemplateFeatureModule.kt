package com.example.feature.templatefeature.wiring.di

import com.example.core.navigation.FeatureEntry
import com.example.feature.templatefeature.api.TemplateFeatureEntry
import com.example.feature.templatefeature.api.TemplateFeatureNavigator
import com.example.feature.templatefeature.api.data.TemplateFeatureRepository
import com.example.feature.templatefeature.api.domain.usecase.GetTemplateFeaturesUseCase
import com.example.feature.templatefeature.impl.TemplateFeatureEntryImpl
import com.example.feature.templatefeature.impl.navigation.TemplateFeatureNavigatorImpl
import com.example.feature.templatefeature.impl.domain.usecase.GetTemplateFeaturesUseCaseImpl
import com.example.feature.templatefeature.impl.data.ApiTemplateFeatureMapper
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class TemplateFeatureModule {

    @Binds
    @Singleton
    abstract fun bindGetTemplateFeaturesUseCase(
        impl: GetTemplateFeaturesUseCaseImpl
    ): GetTemplateFeaturesUseCase

    @Binds
    @IntoSet
    abstract fun bindTemplateFeatureEntry(
        templatefeatureEntryImpl: TemplateFeatureEntryImpl
    ): FeatureEntry

    @Binds
    @Singleton
    abstract fun bindTemplateFeatureNavigator(
        impl: TemplateFeatureNavigatorImpl
    ): TemplateFeatureNavigator

    @Binds
    @Singleton
    abstract fun bindTemplateFeatureEntryAsTemplateFeatureEntry(
        impl: TemplateFeatureEntryImpl
    ): TemplateFeatureEntry

    companion object {
        @Provides
        @Singleton
        fun provideApiTemplateFeatureMapper(): ApiTemplateFeatureMapper = ApiTemplateFeatureMapper()
    }
}
