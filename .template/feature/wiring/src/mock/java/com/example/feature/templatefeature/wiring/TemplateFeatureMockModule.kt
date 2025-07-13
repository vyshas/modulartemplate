package com.example.feature.templatefeature.wiring

import android.content.Context
import com.example.feature.templatefeature.api.data.TemplateFeatureRepository
import com.example.feature.templatefeature.impl.data.ApiTemplateFeatureMapper
import com.example.feature.templatefeature.impl.data.repository.TemplateFeatureRepositoryMockImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object TemplateFeatureMockModule {

    @Provides
    @Singleton
    fun provideTemplateFeatureRepository(
        @ApplicationContext context: Context,
        mapper: ApiTemplateFeatureMapper
    ): TemplateFeatureRepository {
        // Ensure we use application context, which is properly injected by Hilt
        return TemplateFeatureRepositoryMockImpl(context.applicationContext, mapper)
    }
}