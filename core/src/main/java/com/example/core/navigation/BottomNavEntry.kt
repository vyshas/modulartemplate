package com.example.core.navigation

import androidx.fragment.app.Fragment

interface BottomNavEntry : FeatureEntry {
    fun bottomNavPosition(): Int
    fun iconRes(): Int
    fun label(): String
    fun createRootFragment(): Fragment
}
