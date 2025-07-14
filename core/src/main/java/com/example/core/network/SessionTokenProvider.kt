package com.example.core.network

/**
 * Stores and provides a session token dynamically.
 * Can be updated from login response, persisted in storage, etc.
 */
class SessionTokenProvider : TokenProvider {

    private var token: String? = null

    override fun getHeaderKey(): String = "Authorization"

    override fun getToken(): String = token?.let { "Bearer $it" } ?: ""

    fun updateToken(newToken: String) {
        token = newToken
    }

    fun clearToken() {
        token = null
    }
}