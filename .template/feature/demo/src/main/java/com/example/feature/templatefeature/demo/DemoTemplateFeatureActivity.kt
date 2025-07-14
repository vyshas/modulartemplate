package com.example.feature.templatefeature.demo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import androidx.navigation.fragment.NavHostFragment
import com.example.core.navigation.FeatureEntry
import com.example.feature.templatefeature.api.TemplateFeatureEntry
import com.example.feature.templatefeature.demo.databinding.ActivityDemoBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class DemoTemplateFeatureActivity : AppCompatActivity() {

    @Inject
    lateinit var featureEntries: Set<@JvmSuppressWildcards FeatureEntry>

    private lateinit var binding: ActivityDemoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDemoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (savedInstanceState == null) {
            val navHostFragment = NavHostFragment.create(getTemplateFeatureEntry().getGraphResId())
            supportFragmentManager.commit {
                replace(binding.fragmentContainer.id, navHostFragment)
            }
        }
    }

    private fun getTemplateFeatureEntry(): TemplateFeatureEntry {
        return featureEntries.filterIsInstance<TemplateFeatureEntry>().firstOrNull()
            ?: error("TemplateFeatureEntry not found in featureEntries")
    }
}
