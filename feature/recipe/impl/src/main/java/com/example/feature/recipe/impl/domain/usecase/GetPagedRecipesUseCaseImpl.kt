package com.example.feature.recipe.impl.domain.usecase

import androidx.paging.PagingData
import com.example.feature.recipe.api.data.RecipeRepository
import com.example.feature.recipe.api.domain.model.Recipe
import com.example.feature.recipe.api.domain.usecase.GetPagedRecipesUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetPagedRecipesUseCaseImpl @Inject constructor(
    private val repository: RecipeRepository,
) : GetPagedRecipesUseCase {

    override fun getPagedRecipes(): Flow<PagingData<Recipe>> {
        return repository.getPagedRecipes()
    }

}
