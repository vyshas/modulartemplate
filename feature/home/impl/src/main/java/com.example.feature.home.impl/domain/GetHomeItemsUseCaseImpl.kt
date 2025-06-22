package com.example.feature.home.impl.domain

import com.example.core.domain.DomainResult
import com.example.feature.home.api.domain.model.HomeItem
import com.example.feature.home.api.domain.usecase.GetHomeItemsUseCase
import com.example.feature.home.impl.data.repository.HomeRepositoryImpl
import javax.inject.Inject

class GetHomeItemsUseCaseImpl @Inject constructor(
    private val homeRepositoryImpl: HomeRepositoryImpl
) : GetHomeItemsUseCase {

    override suspend fun getHomeItems(): DomainResult<List<HomeItem>> {
        val homeItems = homeRepositoryImpl.getHomeItems()
        if (homeItems is DomainResult.Success) {
            homeRepositoryImpl.cacheItems(homeItems.data)
        }
        return homeItems
    }

}
