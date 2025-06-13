package com.example.core.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController

interface FeatureEntry {
    fun NavGraphBuilder.register(navController: NavHostController)
    fun route(): String
}
