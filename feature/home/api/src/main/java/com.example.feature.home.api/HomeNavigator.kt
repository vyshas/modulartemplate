package com.example.feature.home.api

import android.content.Context
import android.content.Intent

interface HomeNavigator {
    fun intentFor(
        context: Context,
        destination: HomeDestination,
    ): Intent
}
