package com.example.feature.home.impl.ui

import com.example.core.navigation.FeatureEntry
import com.example.core.navigation.FeatureEntryActivity
import com.example.feature.home.api.HomeEntry
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class HomeFeatureActivity : FeatureEntryActivity() {

    @Inject
    lateinit var homeEntry: HomeEntry

    override val entry: FeatureEntry
        get() = homeEntry
}
