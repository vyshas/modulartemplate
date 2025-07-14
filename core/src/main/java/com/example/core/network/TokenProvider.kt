package com.example.core.network

/**
 * Interface to provide authentication headers like tokens or API keys.
 * Each feature can bind its own implementation of this.
 */
interface TokenProvider {
    fun getHeaderKey(): String     // e.g. "Authorization", "x-api-key"
    fun getToken(): String         // e.g. "Bearer abc123" or raw API key
}