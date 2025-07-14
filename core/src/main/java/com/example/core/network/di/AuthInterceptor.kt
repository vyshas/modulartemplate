package com.example.core.network.di

import com.example.core.network.TokenProvider
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject
import javax.inject.Provider

/**
 * Reusable interceptor that attaches auth headers if a TokenProvider is available.
 */
class AuthInterceptor @Inject constructor(
    private val tokenProvider: Provider<TokenProvider>,
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val requestBuilder = chain.request().newBuilder()

        val provider = runCatching { tokenProvider.get() }.getOrNull()
        if (provider != null) {
            val headerKey = provider.getHeaderKey()
            val token = provider.getToken()

            if (headerKey.isNotBlank() && token.isNotBlank()) {
                requestBuilder.addHeader(headerKey, token)
            }
        }

        return chain.proceed(requestBuilder.build())
    }
}
