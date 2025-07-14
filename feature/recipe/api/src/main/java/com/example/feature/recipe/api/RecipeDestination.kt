package com.example.feature.recipe.api

sealed interface RecipeDestination {
    data class Detail(val recipeId: Int) : RecipeDestination
}