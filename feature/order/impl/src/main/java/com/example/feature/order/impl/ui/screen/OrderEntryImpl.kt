package com.example.feature.order.impl.ui.screen

import androidx.fragment.app.Fragment
import androidx.navigation.NavHostController
import com.example.core.navigation.BottomNavEntry
import com.example.feature.order.api.domain.OrderEntry
import com.example.feature.order.impl.R
import javax.inject.Inject

class OrderEntryImpl @Inject constructor() : OrderEntry, BottomNavEntry {

    override fun route(): String = "order"

    override fun getGraphResId(): Int {
        return R.navigation.order_nav_graph
    }

    override fun label(): String = "Order"

    override fun createRootFragment(): Fragment {
        return OrderFragment()
    }

    override fun iconRes(): Int = R.drawable.ic_order

    override fun bottomNavPosition(): Int = 1

    override fun registerGraph(navController: NavHostController) {
        val graph = navController.navInflater.inflate(R.navigation.order_nav_graph)
        navController.setGraph(graph, null)
    }
}

