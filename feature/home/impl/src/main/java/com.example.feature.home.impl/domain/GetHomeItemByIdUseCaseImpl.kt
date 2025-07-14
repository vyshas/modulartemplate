package com.example.feature.home.impl.domain

import com.example.core.domain.DomainResult
import com.example.feature.home.api.data.HomeRepository
import com.example.feature.home.api.domain.model.HomeItem
import com.example.feature.home.api.domain.usecase.GetHomeItemByIdUseCase
import javax.inject.Inject

class GetHomeItemByIdUseCaseImpl @Inject constructor(
    private val repository: HomeRepository,
) : GetHomeItemByIdUseCase {

    override suspend fun invoke(id: Int): DomainResult<HomeItem> {
        return repository.getCachedItemById(id)?.let {
            DomainResult.Success(it)
        } ?: DomainResult.Error("Item not found")
    }
}
