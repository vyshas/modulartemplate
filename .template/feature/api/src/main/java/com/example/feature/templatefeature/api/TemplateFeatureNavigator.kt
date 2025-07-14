package com.example.feature.templatefeature.api

import android.content.Context
import android.content.Intent

interface TemplateFeatureNavigator {
    fun intentFor(
        context: Context,
        destination: TemplateFeatureDestination,
    ): Intent
}
