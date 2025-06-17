package com.example.feature.home.impl.data.repository

import com.example.core.domain.DomainResult
import com.example.feature.home.api.data.HomeRepository
import com.example.feature.home.api.domain.model.HomeItem
import com.example.feature.home.impl.data.api.HomeApi
import com.example.feature.home.impl.data.model.ApiHomeItem
import javax.inject.Inject

class HomeRepositoryImpl @Inject constructor(
    private val api: HomeApi
) : HomeRepository {

    override suspend fun getHomeItems(): DomainResult<List<HomeItem>> {
        return try {
            val apiResult = api.fetchHomeItems()
            DomainResult.Success(apiResult.map { it.toDomain() })
        } catch (e: Exception) {
            DomainResult.Error("Failed to fetch items: ${e.message}")
        }
    }

    private fun ApiHomeItem.toDomain(): HomeItem {
        return HomeItem(id = this.id, title = this.title)
    }
}
