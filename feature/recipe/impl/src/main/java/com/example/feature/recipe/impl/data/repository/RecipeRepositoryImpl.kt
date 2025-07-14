package com.example.feature.recipe.impl.data.repository

import com.example.core.domain.DomainResult
import com.example.feature.recipe.api.data.RecipeRepository
import com.example.feature.recipe.api.domain.model.Recipe
import com.example.feature.recipe.impl.data.ApiRecipeMapper
import com.example.feature.recipe.impl.remote.RecipeApi
import javax.inject.Inject

class RecipeRepositoryImpl @Inject constructor(
    private val api: RecipeApi,
    private val mapper: ApiRecipeMapper,
) : RecipeRepository {

    override suspend fun getRecipes(): DomainResult<List<Recipe>> {
        return try {
            val apiRecipes = api.getRecipes()
            val recipes = mapper.map(apiRecipes)
            DomainResult.Success(recipes)
        } catch (e: Exception) {
            DomainResult.Error("Failed to fetch recipes: ${e.message}")
        }
    }
}
