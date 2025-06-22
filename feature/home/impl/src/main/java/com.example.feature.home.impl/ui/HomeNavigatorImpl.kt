package com.example.feature.home.impl.navigation

import android.content.Context
import android.content.Intent
import com.example.feature.home.api.HomeDestination
import com.example.feature.home.api.HomeNavigator
import com.example.feature.home.impl.ui.homedetail.HomeDetailActivity
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HomeNavigatorImpl @Inject constructor() : HomeNavigator {

    override fun intentFor(context: Context, destination: HomeDestination): Intent {
        return when (destination) {
            is HomeDestination.Detail -> {
                Intent(context, HomeDetailActivity::class.java).apply {
                    putExtra(HomeDetailActivity.KEY_ITEM_ID, destination.homeId)
                }
            }
        }
    }
}
