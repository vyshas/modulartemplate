package com.example.feature.templatefeature.wiring

import com.example.core.network.GlobalRetrofit
import com.example.feature.templatefeature.api.data.TemplateFeatureRepository
import com.example.feature.templatefeature.impl.data.ApiTemplateFeatureMapper
import com.example.feature.templatefeature.impl.data.repository.TemplateFeatureRepositoryImpl
import com.example.feature.templatefeature.impl.remote.TemplateFeatureApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object TemplateFeatureProdModule {

    @Provides
    @Singleton
    fun provideTemplateFeatureApi(
        @GlobalRetrofit retrofit: Retrofit
    ): TemplateFeatureApi {
        return retrofit.create(TemplateFeatureApi::class.java)
    }

    @Provides
    @Singleton
    fun provideTemplateFeatureRepository(
        api: TemplateFeatureApi,
        mapper: ApiTemplateFeatureMapper
    ): TemplateFeatureRepository = TemplateFeatureRepositoryImpl(api, mapper)
}