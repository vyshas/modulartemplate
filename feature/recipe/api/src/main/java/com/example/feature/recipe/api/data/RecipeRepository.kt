package com.example.feature.recipe.api.data

import com.example.core.domain.DomainResult
import com.example.feature.recipe.api.domain.model.Recipe

interface RecipeRepository {
    suspend fun getRecipes(): DomainResult<List<Recipe>>
}
