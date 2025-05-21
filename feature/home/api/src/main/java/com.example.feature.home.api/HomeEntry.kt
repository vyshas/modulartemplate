package com.example.feature.home.api

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController

interface HomeEntry {
    fun NavGraphBuilder.register(navController: NavHostController)
    fun route(): String
}