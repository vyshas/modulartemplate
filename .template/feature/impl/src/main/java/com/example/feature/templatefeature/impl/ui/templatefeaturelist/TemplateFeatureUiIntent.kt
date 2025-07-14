package com.example.feature.templatefeature.impl.ui.templatefeaturelist

sealed interface TemplateFeatureUiIntent {
    object FetchTemplateFeatures : TemplateFeatureUiIntent
    data class TemplateFeatureClicked(val templatefeatureId: Int) : TemplateFeatureUiIntent
}
