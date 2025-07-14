package com.example.feature.recipe.demo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import androidx.navigation.fragment.NavHostFragment
import com.example.core.navigation.FeatureEntry
import com.example.feature.recipe.api.RecipeEntry
import com.example.feature.recipe.demo.databinding.ActivityDemoBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class DemoRecipeActivity : AppCompatActivity() {

    @Inject
    lateinit var featureEntries: Set<@JvmSuppressWildcards FeatureEntry>

    private lateinit var binding: ActivityDemoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDemoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (savedInstanceState == null) {
            val navHostFragment = NavHostFragment.create(getrecipeEntry().getGraphResId())
            supportFragmentManager.commit {
                replace(binding.fragmentContainer.id, navHostFragment)
            }
        }
    }

    private fun getrecipeEntry(): RecipeEntry {
        return featureEntries.filterIsInstance<RecipeEntry>().firstOrNull()
            ?: error("recipeEntry not found in featureEntries")
    }
}
