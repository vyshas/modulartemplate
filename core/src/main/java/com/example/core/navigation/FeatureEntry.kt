package com.example.core.navigation

import androidx.navigation.NavHostController

/**
 * Base interface for all feature entries in the app.
 * Each feature should implement this interface to provide its navigation setup.
 */
interface FeatureEntry {
    /**
     * Registers the feature's navigation graph.
     * @param navController The NavHostController to use for navigation
     */
    fun registerGraph(navController: NavHostController)

    /**
     * Returns the base route for this feature.
     * This should be unique across all features.
     */
    fun route(): String

    fun getGraphResId(): Int
} 