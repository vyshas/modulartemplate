package com.example.feature.templatefeature.impl.ui.templatefeaturelist

import com.example.feature.templatefeature.api.domain.model.TemplateFeature

sealed interface TemplateFeatureUiIntent {
    object FetchTemplateFeatures : TemplateFeatureUiIntent
    data class TemplateFeatureClicked(val templatefeatureId: Int) : TemplateFeatureUiIntent
}