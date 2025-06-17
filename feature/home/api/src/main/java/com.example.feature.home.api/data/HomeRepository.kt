package com.example.feature.home.api.data

import com.example.core.domain.DomainResult
import com.example.feature.home.api.domain.model.HomeItem

interface HomeRepository {
    suspend fun getHomeItems(): DomainResult<List<HomeItem>>
}