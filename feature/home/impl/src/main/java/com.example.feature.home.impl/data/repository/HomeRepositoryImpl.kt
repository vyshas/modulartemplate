package com.example.feature.home.impl.data.repository

import com.example.core.network.ApiResult
import com.example.feature.home.impl.data.api.HomeApi
import com.example.feature.home.impl.data.model.ApiHomeItem
import javax.inject.Inject

class HomeRepositoryImpl @Inject constructor(
    private val api: HomeApi
) {

    suspend fun getHomeItems(): ApiResult<List<ApiHomeItem>> {
        return try {
            ApiResult.Success(api.fetchHomeItems())
        } catch (e: Exception) {
            ApiResult.Error(e.stackTraceToString())
        }
    }
}