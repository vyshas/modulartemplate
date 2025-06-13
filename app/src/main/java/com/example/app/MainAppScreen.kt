package com.example.app

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.compose.DialogNavigator
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.core.navigation.FeatureEntry
import com.example.feature.home.api.HomeEntry

sealed class BottomNavItem(
    val route: String,
    val label: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector
) {
    data object Home : BottomNavItem("home_graph", "Home", Icons.Filled.Home)
    data object Order : BottomNavItem("order", "Order", Icons.Filled.ShoppingCart)
}

@Composable
fun MainAppUI(
    navController: NavHostController,
    featureEntries: Set<@JvmSuppressWildcards FeatureEntry>,
    fragmentContainerFactory: (Context) -> FrameLayout
) {
    val homeEntries = featureEntries.filterIsInstance<HomeEntry>()
    val startDestination = homeEntries.firstOrNull()?.route() ?: BottomNavItem.Home.route
    var selectedRoute by remember { mutableStateOf(startDestination) }

    Box(modifier = Modifier.fillMaxSize()) {

        // Fragment container
        AndroidView(
            factory = fragmentContainerFactory,
            modifier = Modifier.fillMaxSize()
        )

        // Navigation host
        NavHost(
            navController = navController,
            startDestination = startDestination
        ) {
            featureEntries.forEach { entry ->
                with(entry) {
                    this@NavHost.register(navController)
                }
            }

            composable(BottomNavItem.Order.route) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Order Screen - Placeholder")
                }
            }

            if (homeEntries.isEmpty() && startDestination == BottomNavItem.Home.route) {
                composable(BottomNavItem.Home.route) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("Home Screen - No Modules Loaded")
                    }
                }
            }
        }

        // Bottom Navigation
        val bottomNavItems = listOf(BottomNavItem.Home, BottomNavItem.Order)
        BottomNavigation(modifier = Modifier.align(Alignment.BottomCenter)) {
            bottomNavItems.forEach { item ->
                val actualRoute = if (item is BottomNavItem.Home) {
                    homeEntries.firstOrNull()?.route() ?: item.route
                } else {
                    item.route
                }

                BottomNavigationItem(
                    icon = { Icon(item.icon, contentDescription = item.label) },
                    label = { Text(item.label) },
                    selected = selectedRoute == actualRoute,
                    onClick = {
                        selectedRoute = actualRoute
                        navController.navigate(actualRoute) {
                            popUpTo(navController.graph.startDestinationId) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }
        }
    }
}

@Preview(showBackground = true, name = "Main App UI Preview")
@Composable
fun DefaultPreview() {
    val mockContext = LocalContext.current
    val mockNavController = remember {
        NavHostController(mockContext).apply {
            navigatorProvider.addNavigator(DialogNavigator())
            navigatorProvider.addNavigator(ComposeNavigator())
        }
    }

    val mockHomeEntry = object : HomeEntry {
        override fun route(): String = BottomNavItem.Home.route
        override fun NavGraphBuilder.register(navController: NavHostController) {
            composable(route()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Preview: Mock Home Screen")
                }
            }
        }
    }

    val mockEntries = setOf<FeatureEntry>(mockHomeEntry)

    val mockFragmentContainerFactory = { context: Context ->
        FrameLayout(context).apply {
            id = View.generateViewId()
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
        }
    }

    MainAppUI(
        navController = mockNavController,
        featureEntries = mockEntries,
        fragmentContainerFactory = mockFragmentContainerFactory
    )
}
