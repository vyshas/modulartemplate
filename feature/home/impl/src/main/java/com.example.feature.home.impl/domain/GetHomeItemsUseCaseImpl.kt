package com.example.feature.home.impl.domain

import com.example.core.domain.DomainResult
import com.example.core.mapper.toDomainResult
import com.example.feature.home.api.domain.model.HomeItem
import com.example.feature.home.api.domain.usecase.GetHomeItemsUseCase
import com.example.feature.home.impl.data.model.ApiHomeItem
import com.example.feature.home.impl.data.repository.HomeRepositoryImpl
import javax.inject.Inject

class GetHomeItemsUseCaseImpl @Inject constructor(
    private val homeRepositoryImpl: HomeRepositoryImpl
) : GetHomeItemsUseCase {

    override suspend fun getHomeItems(): DomainResult<List<HomeItem>> {
        return homeRepositoryImpl.getHomeItems()
            .toDomainResult { it.toDomain() }
    }

    private fun ApiHomeItem.toDomain(): HomeItem {
        return HomeItem(id = this.id, title = this.title)
    }

    private fun List<ApiHomeItem>.toDomain(): List<HomeItem> = map { it.toDomain() }
}
