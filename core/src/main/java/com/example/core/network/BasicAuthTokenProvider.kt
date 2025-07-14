package com.example.core.network

import android.util.Base64

/**
 * Encodes username and password as a Basic Auth header.
 */
class BasicAuthTokenProvider(
    private val username: String,
    private val password: String
) : TokenProvider {

    override fun getHeaderKey(): String = "Authorization"

    override fun getToken(): String {
        val credentials = "$username:$password"
        val encoded = Base64.encodeToString(
            credentials.toByteArray(),
            Base64.NO_WRAP
        )
        return "Basic $encoded"
    }
}