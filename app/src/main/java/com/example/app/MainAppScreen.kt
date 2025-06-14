package com.example.app

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
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
import androidx.compose.material.icons.filled.Home // Assuming you'll add more icons
import androidx.compose.material.icons.filled.ShoppingCart // Example for Order
import androidx.compose.runtime.*
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
    data object Home : BottomNavItem(
        "home_graph",
        "Home",
        Icons.Filled.Home
    ) // Use a graph route if home is a nested graph

    data object Order : BottomNavItem(
        "order",
        "Order",
        Icons.Filled.ShoppingCart
    )
    // Add more items here
}

@Composable
fun MainAppUI(
    navController: NavHostController,
    featureEntries: Set<@JvmSuppressWildcards FeatureEntry>,
    fragmentContainerFactory: (Context) -> FrameLayout
) {
    val rememberedNavController = remember { navController }
    var selectedRoute by remember { mutableStateOf(BottomNavItem.Home.route) } // Track selected route

    // If homeEntries is empty, use the first entry's route or a default
    val startDestination =
        featureEntries.firstOrNull()?.route() ?: BottomNavItem.Home.route // Fallback needed

    Box(modifier = Modifier.fillMaxSize()) {
        // Fragment container
        AndroidView(
            factory = fragmentContainerFactory, // Pass the function directly
            modifier = Modifier.fillMaxSize()
        )

        // Navigation host
        NavHost(
            navController = rememberedNavController,
            startDestination = startDestination
        ) {
            featureEntries.forEach { entry ->
                entry.run { register(rememberedNavController) }
            }
            composable(BottomNavItem.Order.route) { // Use route from sealed class
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Order Screen - Placeholder")
                }
            }
            // A placeholder if no home entries are registered and startDestination is the default
            if (featureEntries.isEmpty() && startDestination == BottomNavItem.Home.route) {
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
                val actualRoute = if (item == BottomNavItem.Home) {
                    featureEntries.firstOrNull()?.route()
                        ?: item.route // Use actual home entry route if available
                } else {
                    item.route
                }
                BottomNavigationItem(
                    icon = { Icon(item.icon, contentDescription = item.label) },
                    label = { Text(item.label) },
                    selected = selectedRoute == actualRoute || (selectedRoute == BottomNavItem.Home.route && actualRoute == featureEntries.firstOrNull()
                        ?.route()),
                    onClick = {
                        selectedRoute = actualRoute
                        rememberedNavController.navigate(actualRoute) {
                            // Pop up to the start destination of the graph to
                            // avoid building up a large stack of destinations
                            // on the back stack as users select items
                            popUpTo(rememberedNavController.graph.startDestinationId) {
                                saveState = true
                            }
                            // Avoid multiple copies of the same destination when
                            // reselecting the same item
                            launchSingleTop = true
                            // Restore state when reselecting a previously selected item
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
    val mockNavController = NavHostController(mockContext).apply {
        navigatorProvider.addNavigator(DialogNavigator())
        navigatorProvider.addNavigator(ComposeNavigator())
        // For a full preview involving fragments, a more complex FragmentNavigator setup would be needed.
    }

    val mockHomeEntry = object : HomeEntry {
        override fun route(): String = BottomNavItem.Home.route // Consistent with BottomNavItem

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
    val mockEntries = setOf(mockHomeEntry)

    val mockFragmentContainerFactory = { context: Context ->
        FrameLayout(context).apply {
            id = View.generateViewId() // Important for FragmentNavigator
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
        }
    }

    // Wrap in a Theme if you have one for consistent preview
    // YourAppTheme {
    MainAppUI(
        navController = mockNavController,
        featureEntries = mockEntries,
        fragmentContainerFactory = mockFragmentContainerFactory
    )
    // }
}