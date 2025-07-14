package com.example.feature.recipe.impl.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import com.example.feature.recipe.impl.R
import com.example.feature.recipe.impl.ui.recipelist.RecipeListFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RecipeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recipe)

        if (savedInstanceState == null) {
            supportFragmentManager.commit {
                replace(R.id.fragment_container, RecipeListFragment())
            }
        }
    }
}
