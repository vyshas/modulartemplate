package com.example.feature.recipe.api.domain.model

data class Recipe(
    val caloriesPerServing: Int,
    val cookTimeMinutes: Int,
    val cuisine: String,
    val difficulty: String,
    val id: Int,
    val ingredients: List<String>,
    val instructions: List<String>,
    val mealType: List<String>,
    val name: String,
)
