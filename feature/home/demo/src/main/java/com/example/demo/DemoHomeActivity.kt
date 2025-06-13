package com.example.demo

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavHostController
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.compose.DialogNavigator
import androidx.navigation.compose.NavHost
import androidx.navigation.fragment.FragmentNavigator
import com.example.feature.home.api.HomeEntry
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class DemoHomeActivity : AppCompatActivity() {

    @Inject
    lateinit var homeEntries: Set<@JvmSuppressWildcards HomeEntry>

    private lateinit var navController: NavHostController
    private var containerId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupNavigation()
        setupContent()
    }

    private fun setupNavigation() {
        containerId = View.generateViewId()
        navController = createNavController()
    }

    private fun createNavController(): NavHostController {
        return NavHostController(this).apply {
            setupNavigators(this)
        }
    }

    private fun setupNavigators(controller: NavHostController) {
        with(controller.navigatorProvider) {
            addNavigator(DialogNavigator())
            addNavigator(ComposeNavigator())
            addNavigator(createFragmentNavigator())
        }
    }

    private fun createFragmentNavigator(): FragmentNavigator {
        return FragmentNavigator(
            this@DemoHomeActivity,
            supportFragmentManager,
            containerId
        )
    }

    private fun createFragmentContainer(context: android.content.Context): FrameLayout {
        return FrameLayout(context).apply {
            id = containerId
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
        }
    }

    private fun setupContent() {
        setContent {
            val rememberedNavController = remember { navController }
            val homeEntry = homeEntries.first()

            Box(modifier = Modifier.fillMaxSize()) {
                // Fragment container
                AndroidView(
                    factory = { context -> createFragmentContainer(context) },
                    modifier = Modifier.fillMaxSize()
                )

                // Navigation host
                NavHost(
                    navController = rememberedNavController,
                    startDestination = homeEntry.route()
                ) {
                    homeEntry.run { register(rememberedNavController) }
                }
            }
        }
    }
}