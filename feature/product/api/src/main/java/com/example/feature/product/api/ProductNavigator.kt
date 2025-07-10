package com.example.feature.product.api

import android.content.Context
import android.content.Intent

interface ProductNavigator {
    fun intentFor(
        context: Context,
        destination: ProductDestination
    ): Intent
}