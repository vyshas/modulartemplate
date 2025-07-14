package com.example.feature.templatefeature.impl.ui.templatefeaturelist

import com.example.feature.templatefeature.api.domain.model.TemplateFeature

sealed class TemplateFeatureUiState {
    object Loading : TemplateFeatureUiState()
    data class Success(val templatefeatures: List<TemplateFeature>) : TemplateFeatureUiState()
    data class Error(val message: String) : TemplateFeatureUiState()
}
