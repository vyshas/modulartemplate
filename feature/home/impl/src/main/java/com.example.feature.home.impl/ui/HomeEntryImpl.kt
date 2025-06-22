package com.example.feature.home.impl.ui

import androidx.fragment.app.Fragment
import androidx.navigation.NavHostController
import com.example.core.navigation.BottomNavEntry
import com.example.feature.home.api.HomeEntry
import com.example.feature.home.impl.R
import com.example.feature.home.impl.ui.homelist.HomeFragment
import javax.inject.Inject

class HomeEntryImpl @Inject constructor() : HomeEntry, BottomNavEntry {

    override fun registerGraph(navController: NavHostController) {
        val navGraph = navController.navInflater.inflate(R.navigation.home_nav_graph)
        navController.setGraph(navGraph, null)
    }

    override fun route(): String = "home"

    override fun getGraphResId(): Int {
        return R.navigation.home_nav_graph
    }

    override fun label(): String = "Home"

    override fun iconRes(): Int = R.drawable.ic_home

    override fun bottomNavPosition(): Int = 0

    override fun createRootFragment(): Fragment = HomeFragment()
}
