package com.example.feature.recipe.impl.data.repository

import android.content.Context
import com.example.core.domain.DomainResult
import com.example.core.network.parseJsonFromAssets
import com.example.feature.recipe.api.data.RecipeRepository
import com.example.feature.recipe.api.domain.model.Recipe
import com.example.feature.recipe.impl.data.ApiRecipeListResponse
import com.example.feature.recipe.impl.data.ApiRecipeMapper
import javax.inject.Inject

class RecipeRepositoryMockImpl @Inject constructor(
    private val context: Context,
    private val mapper: ApiRecipeMapper,
) : RecipeRepository {

    override suspend fun getRecipes(): DomainResult<List<Recipe>> {
        return try {
            val response = parseJsonFromAssets<ApiRecipeListResponse>(
                context,
                "recipe_mock.json",
            )
            val recipes: List<Recipe> = response?.let { mapper.map(it) } ?: emptyList()
            DomainResult.Success(recipes)
        } catch (e: Exception) {
            DomainResult.Error("Failed to load mock recipes: ${e.message}")
        }
    }
}
