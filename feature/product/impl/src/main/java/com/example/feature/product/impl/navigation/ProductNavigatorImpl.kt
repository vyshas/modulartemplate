package com.example.feature.product.impl.navigation

import android.content.Context
import android.content.Intent
import com.example.feature.product.api.ProductDestination
import com.example.feature.product.api.ProductNavigator
import javax.inject.Inject

class ProductNavigatorImpl @Inject constructor() : ProductNavigator {
    override fun intentFor(
        context: Context,
        destination: ProductDestination
    ): Intent {
        // TODO: Properly route to a product detail/activity if needed
        return Intent()
    }
}