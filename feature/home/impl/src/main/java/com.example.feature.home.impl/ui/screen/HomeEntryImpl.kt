package com.example.feature.home.impl.ui.screen

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.fragment.fragment
import com.example.feature.home.api.HomeEntry
import com.example.feature.home.api.HomeRoute
import com.squareup.anvil.annotations.ContributesMultibinding
import dagger.hilt.components.SingletonComponent
import javax.inject.Inject

@ContributesMultibinding(SingletonComponent::class)
class HomeEntryImpl @Inject constructor() : HomeEntry {

    override fun NavGraphBuilder.register(
        navController: NavHostController
    ) {
        fragment<HomeFragment>(route())
    }

    override fun route(): String = HomeRoute.ROUTE
}