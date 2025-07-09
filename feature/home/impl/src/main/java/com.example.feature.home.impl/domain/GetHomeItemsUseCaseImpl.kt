package com.example.feature.home.impl.domain

import com.example.core.domain.DomainResult
import com.example.feature.home.api.data.HomeRepository
import com.example.feature.home.api.domain.model.HomeItem
import com.example.feature.home.api.domain.usecase.GetHomeItemsUseCase
import javax.inject.Inject

class GetHomeItemsUseCaseImpl @Inject constructor(
    private val homeRepository: HomeRepository
) : GetHomeItemsUseCase {

    override suspend fun getHomeItems(): DomainResult<List<HomeItem>> {
        return homeRepository.getHomeItems().also { result ->
            if (result is DomainResult.Success) {
                homeRepository.cacheItems(result.data)
            }
        }
    }
}
