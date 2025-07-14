package com.example.core.mapper

import com.example.core.domain.DomainResult
import com.example.core.network.ApiResult

fun <A, D> ApiResult<A>.toDomainResult(mapper: (A) -> D): DomainResult<D> =
    when (this) {
        is ApiResult.Success -> DomainResult.Success(mapper(data))
        is ApiResult.Error -> DomainResult.Error(message)
        is ApiResult.NetworkError -> DomainResult.NetworkError
    }
