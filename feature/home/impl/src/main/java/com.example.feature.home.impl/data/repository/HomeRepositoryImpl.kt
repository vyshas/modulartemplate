package com.example.feature.home.impl.data.repository

import com.example.core.network.ApiResult
import com.example.feature.home.impl.data.api.HomeApi
import com.example.feature.home.impl.data.model.ApiHomeItem
import com.example.feature.home.impl.di.HomeScope
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject

@ContributesBinding(scope = HomeScope::class)
class HomeRepositoryImpl @Inject constructor(
    private val api: HomeApi
) : HomeRepository {

    override fun getHomeItems(): ApiResult<List<ApiHomeItem>> {
        return try {
            ApiResult.Success(api.fetchHomeItems())
        } catch (e: Exception) {
            ApiResult.Error(e.stackTraceToString())
        }
    }
}