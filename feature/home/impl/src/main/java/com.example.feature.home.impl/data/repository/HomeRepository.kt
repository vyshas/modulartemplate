package com.example.feature.home.impl.data.repository

import com.example.core.network.ApiResult
import com.example.feature.home.impl.data.model.ApiHomeItem

interface HomeRepository {
    fun getHomeItems(): ApiResult<List<ApiHomeItem>>
}