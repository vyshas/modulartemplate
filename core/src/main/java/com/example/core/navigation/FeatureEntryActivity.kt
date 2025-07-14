package com.example.core.navigation

import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

/**
 * Base activity that can be extended by any feature to expose a screen.
 * It hosts a NavHostFragment that loads the feature's internal nav graph.
 */
@AndroidEntryPoint
abstract class FeatureEntryActivity : AppCompatActivity() {

    @Inject
    lateinit var featureEntries: Set<@JvmSuppressWildcards FeatureEntry>

    protected abstract val entry: FeatureEntry

    private val containerId = View.generateViewId()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Create a fragment container dynamically
        val container = FrameLayout(this).apply {
            id = containerId
            layoutParams = FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT,
            )
        }
        setContentView(container)

        if (savedInstanceState == null) {
            val navHostFragment = NavHostFragment.create(entry.getGraphResId())
            supportFragmentManager.beginTransaction()
                .replace(containerId, navHostFragment)
                .commit()
        }
    }
}
