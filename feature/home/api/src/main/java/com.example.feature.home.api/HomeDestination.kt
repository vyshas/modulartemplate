package com.example.feature.home.api

sealed interface HomeDestination {
    data class Detail(val homeId: Int) : HomeDestination
}
