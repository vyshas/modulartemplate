package com.example.feature.recipe.impl.data

data class ApiRecipeListResponse(
    val limit: Int,
    val recipes: List<ApiRecipe>,
    val skip: Int,
    val total: Int
)

data class ApiRecipe(
    val caloriesPerServing: Int,
    val cookTimeMinutes: Int,
    val cuisine: String,
    val difficulty: String,
    val id: Int,
    val image: String,
    val ingredients: List<String>,
    val instructions: List<String>,
    val mealType: List<String>,
    val name: String,
    val prepTimeMinutes: Int,
    val rating: Double,
    val reviewCount: Int,
    val servings: Int,
    val tags: List<String>,
    val userId: Int
)