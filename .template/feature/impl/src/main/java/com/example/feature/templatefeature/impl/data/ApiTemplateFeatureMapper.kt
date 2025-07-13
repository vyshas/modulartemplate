package com.example.feature.templatefeature.impl.data

import com.example.core.mapper.Mapper
import com.example.feature.templatefeature.api.domain.model.TemplateFeature
import com.example.feature.templatefeature.api.domain.model.Rating

class ApiTemplateFeatureMapper : Mapper<ApiTemplateFeature, TemplateFeature> {
    override fun map(from: ApiTemplateFeature): TemplateFeature = TemplateFeature(
        id = from.id,
        title = from.title,
        price = from.price,
        description = from.description,
        category = from.category,
        image = from.image,
        rating = Rating(
            rate = from.rating.rate,
            count = from.rating.count
        )
    )
}