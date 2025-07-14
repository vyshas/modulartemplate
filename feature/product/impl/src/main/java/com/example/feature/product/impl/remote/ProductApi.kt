package com.example.feature.product.impl.remote

import com.example.feature.product.impl.data.ApiProduct
import retrofit2.http.GET

interface ProductApi {
    @GET("products")
    suspend fun getProducts(): List<ApiProduct>
}
