package com.example.feature.recipe.api.domain.usecase

import androidx.paging.PagingData
import com.example.core.domain.DomainResult
import com.example.feature.recipe.api.domain.model.Recipe
import kotlinx.coroutines.flow.Flow

interface GetPagedRecipesUseCase {
    fun getPagedRecipes(): Flow<PagingData<Recipe>>
}
