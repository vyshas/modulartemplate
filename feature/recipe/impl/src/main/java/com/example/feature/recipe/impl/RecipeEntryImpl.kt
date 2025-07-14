package com.example.feature.recipe.impl

import androidx.fragment.app.Fragment
import androidx.navigation.NavHostController
import com.example.feature.recipe.api.RecipeEntry
import com.example.feature.recipe.impl.ui.recipelist.RecipeListFragment
import javax.inject.Inject

class RecipeEntryImpl @Inject constructor() : RecipeEntry {

    override fun registerGraph(navController: NavHostController) {
        val navGraph = navController.navInflater.inflate(R.navigation.recipe_nav_graph)
        navController.setGraph(navGraph, null)
    }

    override fun route(): String = "recipe"

    override fun getGraphResId(): Int {
        return R.navigation.recipe_nav_graph
    }

    fun createRootFragment(): Fragment = RecipeListFragment()
}