package com.example.feature.templatefeature.impl.domain.usecase

import com.example.core.domain.DomainResult
import com.example.feature.templatefeature.api.data.TemplateFeatureRepository
import com.example.feature.templatefeature.api.domain.model.TemplateFeature
import com.example.feature.templatefeature.api.domain.usecase.GetTemplateFeaturesUseCase
import javax.inject.Inject

class GetTemplateFeaturesUseCaseImpl @Inject constructor(
    private val repository: TemplateFeatureRepository,
) : GetTemplateFeaturesUseCase {

    override suspend fun getTemplateFeatures(): DomainResult<List<TemplateFeature>> {
        return repository.getTemplateFeatures()
    }
}
