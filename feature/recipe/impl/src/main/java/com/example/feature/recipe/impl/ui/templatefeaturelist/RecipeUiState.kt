package com.example.feature.recipe.impl.ui.recipelist

import com.example.feature.recipe.api.domain.model.Recipe

sealed class RecipeUiState {
    object Loading : RecipeUiState()
    data class Success(val Recipes: List<Recipe>) : RecipeUiState()
    data class Error(val message: String) : RecipeUiState()
}