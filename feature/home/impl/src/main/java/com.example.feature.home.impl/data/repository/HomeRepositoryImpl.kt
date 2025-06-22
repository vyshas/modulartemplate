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

    // Simple in-memory cache
    private val cache = mutableMapOf<Int, HomeItem>()

    override suspend fun getHomeItems(): DomainResult<List<HomeItem>> {
        return try {
            val apiResult = api.fetchHomeItems()
            DomainResult.Success(apiResult.map { it.toDomain() })
        } catch (e: Exception) {
            DomainResult.Error("Failed to fetch items: ${e.message}")
        }
    }

    override suspend fun getCachedItemById(id: Int): HomeItem? {
        return cache[id]
    }

    override fun cacheItems(items: List<HomeItem>) {
        items.forEach { cache[it.id] = it }
    }

    private fun ApiHomeItem.toDomain(): HomeItem {
        return HomeItem(id = this.id, title = this.title)
    }
}
