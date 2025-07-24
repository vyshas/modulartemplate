package com.example.feature.recipe.api.data

import androidx.paging.PagingData
import com.example.core.domain.DomainResult
import com.example.feature.recipe.api.domain.model.Recipe
import kotlinx.coroutines.flow.Flow

interface RecipeRepository {
    suspend fun getRecipes(): DomainResult<List<Recipe>>
    fun getPagedRecipes(): Flow<PagingData<Recipe>>
}
