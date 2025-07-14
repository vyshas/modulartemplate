package com.example.feature.templatefeature.impl.remote

import com.example.feature.templatefeature.impl.data.ApiTemplateFeature
import retrofit2.http.GET

interface TemplateFeatureApi {
    @GET("templatefeatures")
    suspend fun getTemplateFeatures(): List<ApiTemplateFeature>
}
