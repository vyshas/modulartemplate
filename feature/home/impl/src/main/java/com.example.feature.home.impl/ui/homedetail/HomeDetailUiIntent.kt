package com.example.feature.home.impl.ui.homedetail

sealed interface HomeDetailUiIntent {
    data class FetchItem(val itemId: Int) : HomeDetailUiIntent
}
