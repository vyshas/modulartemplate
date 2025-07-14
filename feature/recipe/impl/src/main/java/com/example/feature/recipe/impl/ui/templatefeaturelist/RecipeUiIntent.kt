package com.example.feature.recipe.impl.ui.recipelist

sealed interface RecipeUiIntent {
    object FetchRecipes : RecipeUiIntent
    data class RecipeClicked(val recipeId: Int) : RecipeUiIntent
}
