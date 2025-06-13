package com.example.app

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavHostController
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.compose.DialogNavigator
import androidx.navigation.fragment.FragmentNavigator
import com.example.core.navigation.FeatureEntry
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var featureEntries: Set<@JvmSuppressWildcards FeatureEntry>

    private lateinit var navController: NavHostController
    private var fragmentContainerId: Int = View.NO_ID // Initialize with a sensible default

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupNavigation()
        setupContent()
    }

    private fun setupNavigation() {
        fragmentContainerId = View.generateViewId() // Generate ID for the fragment container
        navController = NavHostController(this).apply {
            navigatorProvider.addNavigator(DialogNavigator())
            navigatorProvider.addNavigator(ComposeNavigator())
            navigatorProvider.addNavigator(
                FragmentNavigator(
                    this@MainActivity,
                    supportFragmentManager,
                    fragmentContainerId // Use the generated ID
                )
            )
        }
    }

    private fun createFragmentContainer(context: Context): FrameLayout {
        return FrameLayout(context).apply {
            id = fragmentContainerId // Use the generated ID consistently
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
        }
    }

    private fun setupContent() {
        setContent {
            MainAppUI(
                navController = navController,
                featureEntries = featureEntries,
                fragmentContainerFactory = this::createFragmentContainer
            )
        }
    }
}
