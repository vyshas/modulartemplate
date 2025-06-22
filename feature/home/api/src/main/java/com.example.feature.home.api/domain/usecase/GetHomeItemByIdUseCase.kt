package com.example.feature.home.api.domain.usecase

import com.example.core.domain.DomainResult
import com.example.feature.home.api.domain.model.HomeItem

interface GetHomeItemByIdUseCase {
    suspend operator fun invoke(id: Int): DomainResult<HomeItem>
}