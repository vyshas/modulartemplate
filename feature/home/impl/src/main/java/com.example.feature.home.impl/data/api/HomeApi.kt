package com.example.feature.home.impl.data.api

import com.example.feature.home.impl.data.model.ApiHomeItem

class HomeApi {
    fun fetchHomeItems(): List<ApiHomeItem> =
        listOf(ApiHomeItem(1, "Item 1"), ApiHomeItem(2, "Item 2"))
}
