package com.example.feature.home.impl.data.repository

import com.example.core.domain.DomainResult
import com.example.feature.home.api.data.HomeRepository
import com.example.feature.home.api.domain.model.HomeItem
import com.example.feature.home.impl.data.api.HomeApi
import com.example.feature.home.impl.data.model.ApiHomeItem
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import javax.inject.Inject

class HomeRepositoryImpl @Inject constructor(
    private val api: HomeApi,
) : HomeRepository {

    // Simple in-memory cache
    private val cache = java.util.concurrent.ConcurrentHashMap<Int, HomeItem>()
    private val cacheMutex = Mutex()

    override suspend fun getHomeItems(): DomainResult<List<HomeItem>> {
        return try {
            val apiResult = api.fetchHomeItems()
            val domainItems = apiResult.map { it.toDomain() }
            DomainResult.Success(domainItems)
        } catch (e: Exception) {
            DomainResult.Error("Failed to fetch items: ${e.message}")
        }
    }

    override suspend fun getCachedItemById(id: Int): HomeItem? {
        return cache[id]
    }

    override suspend fun cacheItems(items: List<HomeItem>) {
        cacheMutex.withLock {
            items.forEach { cache[it.id] = it }
        }
    }

    private fun ApiHomeItem.toDomain(): HomeItem {
        return HomeItem(id = this.id, title = this.title)
    }
}
