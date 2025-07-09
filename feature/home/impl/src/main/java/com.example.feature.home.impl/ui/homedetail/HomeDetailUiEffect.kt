package com.example.feature.home.impl.ui.homedetail

sealed interface HomeDetailUiEffect {
    data class ShowToast(val message: String) : HomeDetailUiEffect
}
