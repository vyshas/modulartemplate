package com.example.feature.recipe.impl.navigation

import android.content.Context
import android.content.Intent
import com.example.feature.recipe.api.RecipeDestination
import com.example.feature.recipe.api.RecipeNavigator
import javax.inject.Inject

class RecipeNavigatorImpl @Inject constructor() : RecipeNavigator {
    override fun intentFor(
        context: Context,
        destination: RecipeDestination,
    ): Intent {
        // TODO: Properly route to a recipe detail/activity if needed
        return Intent()
    }
}
