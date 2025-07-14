package com.example.feature.recipe.impl.domain.usecase

import com.example.core.domain.DomainResult
import com.example.feature.recipe.api.data.RecipeRepository
import com.example.feature.recipe.api.domain.model.Recipe
import com.example.feature.recipe.api.domain.usecase.GetRecipesUseCase
import javax.inject.Inject

class GetRecipesUseCaseImpl @Inject constructor(
    private val repository: RecipeRepository,
) : GetRecipesUseCase {

    override suspend fun getrecipes(): DomainResult<List<Recipe>> {
        return repository.getRecipes()
    }
}
