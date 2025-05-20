package com.example.feature.home.api.domain.usecase

import com.example.core.domain.DomainResult
import com.example.feature.home.api.domain.model.HomeItem

interface GetHomeItemsUseCase {
    suspend fun getHomeItems(): DomainResult<List<HomeItem>>
}
