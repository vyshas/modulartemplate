package com.example.core.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController

/**
 * Base interface for all feature entries in the app.
 * Each feature should implement this interface to provide its navigation setup.
 */
interface FeatureEntry {
    /**
     * Registers the feature's navigation graph with the provided NavGraphBuilder.
     * @param navController The NavHostController to use for navigation
     */
    fun NavGraphBuilder.register(navController: NavHostController)

    /**
     * Returns the base route for this feature.
     * This should be unique across all features.
     */
    fun route(): String
} 