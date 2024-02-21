package com.android.salamandra.data.network.interceptor

import com.android.salamandra.data.model.TokenManager
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AuthInterceptor @Inject constructor(
    private val tokenManager: TokenManager
): Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val token = tokenManager.getToken() ?: return chain.proceed(chain.request())
        val request = chain.request().newBuilder()
        request.addHeader("Authorization", "Bearer $token")
        return chain.proceed(request.build())
    }
}