package com.example.feature.product.impl

import androidx.fragment.app.Fragment
import androidx.navigation.NavHostController
import com.example.feature.product.api.ProductEntry
import com.example.feature.product.impl.ui.productlist.ProductListFragment
import javax.inject.Inject

class ProductEntryImpl @Inject constructor() : ProductEntry {

    override fun registerGraph(navController: NavHostController) {
        val navGraph = navController.navInflater.inflate(R.navigation.product_nav_graph)
        navController.setGraph(navGraph, null)
    }

    override fun route(): String = "product"

    override fun getGraphResId(): Int {
        return R.navigation.product_nav_graph
    }

    fun createRootFragment(): Fragment = ProductListFragment()
}