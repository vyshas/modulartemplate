package com.example.feature.templatefeature.api

sealed interface TemplateFeatureDestination {
    data class Detail(val templatefeatureId: Int) : TemplateFeatureDestination
}