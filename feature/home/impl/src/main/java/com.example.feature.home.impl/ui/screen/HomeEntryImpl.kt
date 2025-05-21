package com.example.feature.home.impl.ui.screen

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.fragment.fragment
import com.example.feature.home.api.domain.HomeEntry
import javax.inject.Inject

class HomeEntryImpl @Inject constructor() : HomeEntry {

    override fun NavGraphBuilder.register(navController: NavHostController) {
        fragment<HomeFragment>(route())
    }

    override fun route(): String = "home"
}