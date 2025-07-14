package com.example.feature.templatefeature.impl.navigation

import android.content.Context
import android.content.Intent
import com.example.feature.templatefeature.api.TemplateFeatureDestination
import com.example.feature.templatefeature.api.TemplateFeatureNavigator
import javax.inject.Inject

class TemplateFeatureNavigatorImpl @Inject constructor() : TemplateFeatureNavigator {
    override fun intentFor(
        context: Context,
        destination: TemplateFeatureDestination,
    ): Intent {
        // TODO: Properly route to a templatefeature detail/activity if needed
        return Intent()
    }
}
