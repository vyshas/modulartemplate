package com.example.feature.product.impl.data

import com.example.core.mapper.Mapper
import com.example.feature.product.api.domain.model.Product
import com.example.feature.product.api.domain.model.Rating

class ApiProductMapper : Mapper<ApiProduct, Product> {
    override fun map(from: ApiProduct): Product = Product(
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