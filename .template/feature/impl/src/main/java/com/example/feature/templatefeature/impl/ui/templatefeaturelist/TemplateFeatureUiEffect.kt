package com.example.feature.templatefeature.impl.ui.templatefeaturelist

sealed interface TemplateFeatureUiEffect {
    data class ShowToast(val message: String) : TemplateFeatureUiEffect
    data class NavigateToTemplateFeatureDetail(val templatefeatureId: Int) : TemplateFeatureUiEffect
}