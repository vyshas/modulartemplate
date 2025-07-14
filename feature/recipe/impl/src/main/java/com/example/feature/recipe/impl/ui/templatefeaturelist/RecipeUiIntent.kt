package com.example.feature.recipe.impl.ui.recipelist

sealed interface RecipeUiIntent {
    object Fetchrecipes : RecipeUiIntent
    data class recipeClicked(val recipeId: Int) : RecipeUiIntent
}