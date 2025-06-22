package com.example.demo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import androidx.navigation.fragment.NavHostFragment
import com.example.core.navigation.FeatureEntry
import com.example.demo.databinding.ActivityDemoBinding
import com.example.feature.home.api.HomeEntry
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class DemoHomeActivity : AppCompatActivity() {

    @Inject
    lateinit var featureEntries: Set<@JvmSuppressWildcards FeatureEntry>

    private lateinit var binding: ActivityDemoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDemoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (savedInstanceState == null) {
            val navHostFragment = NavHostFragment.create(getHomeEntry().getGraphResId())
            supportFragmentManager.commit {
                replace(binding.fragmentContainer.id, navHostFragment)
            }
        }
    }

    private fun getHomeEntry(): HomeEntry {
        return featureEntries.filterIsInstance<HomeEntry>().firstOrNull()
            ?: error("HomeEntry not found in featureEntries")
    }
}
