package com.example.feature.recipe.impl.data

import com.example.core.mapper.Mapper
import com.example.feature.recipe.api.domain.model.Recipe

class ApiRecipeMapper : Mapper<ApiRecipeListResponse, List<Recipe>> {
    override fun map(from: ApiRecipeListResponse): List<Recipe> {
        return from.recipes.map { recipeRaw ->
            Recipe(
                caloriesPerServing = recipeRaw.caloriesPerServing,
                cookTimeMinutes = recipeRaw.cookTimeMinutes,
                cuisine = recipeRaw.cuisine,
                difficulty = recipeRaw.difficulty,
                id = recipeRaw.id,
                ingredients = recipeRaw.ingredients,
                instructions = recipeRaw.instructions,
                mealType = recipeRaw.mealType,
                name = recipeRaw.name,
            )
        }
    }
}
