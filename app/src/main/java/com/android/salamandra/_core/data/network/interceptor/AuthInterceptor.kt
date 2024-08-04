package com.android.salamandra._core.data.network.interceptor

import com.android.salamandra._core.data.cognito.CognitoService
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AuthInterceptor @Inject constructor(
    private val cognitoService: CognitoService
): Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val token = runBlocking {
            cognitoService.getAccessToken()
        }
        if(token == null) {
            return chain.proceed(chain.request())
        } else {
            val request = chain.request().newBuilder()
            request.addHeader("Authorization", "Bearer $token")
            return chain.proceed(request.build())
        }
    }
}