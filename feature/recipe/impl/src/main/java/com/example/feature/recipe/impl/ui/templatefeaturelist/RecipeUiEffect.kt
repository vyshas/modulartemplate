package com.example.feature.recipe.impl.ui.recipelist

sealed interface RecipeUiEffect {
    data class ShowToast(val message: String) : RecipeUiEffect
    data class NavigateToRecipeDetail(val recipeId: Int) : RecipeUiEffect
}