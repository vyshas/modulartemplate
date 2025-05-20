package com.example.core.domain

sealed class DomainResult<out T> {
    data class Success<out T>(val data: T) : DomainResult<T>()
    data class Error(val message: String) : DomainResult<Nothing>()
    data object NetworkError : DomainResult<Nothing>()
}
