package com.example.feature.recipe.api.domain.usecase

import com.example.core.domain.DomainResult
import com.example.feature.recipe.api.domain.model.Recipe

interface GetRecipesUseCase {
    suspend fun getrecipes(): DomainResult<List<Recipe>>
}