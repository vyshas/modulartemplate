package com.example.feature.templatefeature.impl.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import com.example.feature.templatefeature.impl.R
import com.example.feature.templatefeature.impl.ui.templatefeaturelist.TemplateFeatureListFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TemplateFeatureActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_templatefeature)

        if (savedInstanceState == null) {
            supportFragmentManager.commit {
                replace(R.id.fragment_container, TemplateFeatureListFragment())
            }
        }
    }
}
