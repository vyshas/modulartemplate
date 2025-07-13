package com.example.feature.templatefeature.impl.repository

import com.example.core.domain.DomainResult
import com.example.feature.templatefeature.api.domain.model.TemplateFeature
import com.example.feature.templatefeature.api.data.TemplateFeatureRepository
import com.example.feature.templatefeature.impl.data.ApiTemplateFeatureMapper
import com.example.feature.templatefeature.impl.remote.TemplateFeatureApi
import javax.inject.Inject

class TemplateFeatureRepositoryImpl @Inject constructor(
    private val api: TemplateFeatureApi,
    private val mapper: ApiTemplateFeatureMapper
) : TemplateFeatureRepository {

    override suspend fun getTemplateFeatures(): DomainResult<List<TemplateFeature>> {
        return try {
            val apiTemplateFeatures = api.getTemplateFeatures()
            val templatefeatures = apiTemplateFeatures.map { mapper.map(it) }
            DomainResult.Success(templatefeatures)
        } catch (e: Exception) {
            DomainResult.Error("Failed to fetch templatefeatures: ${e.message}")
        }
    }
}