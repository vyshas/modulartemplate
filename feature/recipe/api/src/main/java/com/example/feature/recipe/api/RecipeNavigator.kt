package com.example.feature.recipe.api

import android.content.Context
import android.content.Intent

interface RecipeNavigator {
    fun intentFor(
        context: Context,
        destination: RecipeDestination
    ): Intent
}