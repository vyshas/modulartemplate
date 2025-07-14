package com.example.feature.templatefeature.impl

import androidx.fragment.app.Fragment
import androidx.navigation.NavHostController
import com.example.feature.templatefeature.api.TemplateFeatureEntry
import com.example.feature.templatefeature.impl.ui.templatefeaturelist.TemplateFeatureListFragment
import javax.inject.Inject

class TemplateFeatureEntryImpl @Inject constructor() : TemplateFeatureEntry {

    override fun registerGraph(navController: NavHostController) {
        val navGraph = navController.navInflater.inflate(R.navigation.templatefeature_nav_graph)
        navController.setGraph(navGraph, null)
    }

    override fun route(): String = "templatefeature"

    override fun getGraphResId(): Int {
        return R.navigation.templatefeature_nav_graph
    }

    fun createRootFragment(): Fragment = TemplateFeatureListFragment()
}
