package com.example.feature.templatefeature.api.data

import com.example.core.domain.DomainResult
import com.example.feature.templatefeature.api.domain.model.TemplateFeature

interface TemplateFeatureRepository {
    suspend fun getTemplateFeatures(): DomainResult<List<TemplateFeature>>
}