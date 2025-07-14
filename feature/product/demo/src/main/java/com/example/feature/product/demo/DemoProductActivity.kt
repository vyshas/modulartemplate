package com.example.feature.product.demo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import androidx.navigation.fragment.NavHostFragment
import com.example.core.navigation.FeatureEntry
import com.example.feature.product.api.ProductEntry
import com.example.feature.product.demo.databinding.ActivityDemoBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class DemoProductActivity : AppCompatActivity() {

    @Inject
    lateinit var featureEntries: Set<@JvmSuppressWildcards FeatureEntry>

    private lateinit var binding: ActivityDemoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDemoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (savedInstanceState == null) {
            val navHostFragment = NavHostFragment.create(getProductEntry().getGraphResId())
            supportFragmentManager.commit {
                replace(binding.fragmentContainer.id, navHostFragment)
            }
        }
    }

    private fun getProductEntry(): ProductEntry {
        return featureEntries.filterIsInstance<ProductEntry>().firstOrNull()
            ?: error("ProductEntry not found in featureEntries")
    }
}
