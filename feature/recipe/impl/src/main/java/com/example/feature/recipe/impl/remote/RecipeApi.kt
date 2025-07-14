package com.example.feature.recipe.impl.remote

import com.example.feature.recipe.impl.data.ApiRecipeListResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface RecipeApi {
    @GET("recipes")
    suspend fun getRecipes(
        @Query("limit") limit: Int = 10,
    ): ApiRecipeListResponse
}
