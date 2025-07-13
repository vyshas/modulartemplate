package com.example.feature.templatefeature.api.domain.usecase

import com.example.core.domain.DomainResult
import com.example.feature.templatefeature.api.domain.model.TemplateFeature

interface GetTemplateFeaturesUseCase {
    suspend fun getTemplateFeatures(): DomainResult<List<TemplateFeature>>
}