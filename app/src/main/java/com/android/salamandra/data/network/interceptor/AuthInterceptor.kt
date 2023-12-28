package com.android.salamandra.data.network.interceptor

import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AuthInterceptor @Inject constructor(): Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder().header("X-Api-Key", "7+ILPeshg6BfA2tBVxVtXg==6jy1PFXbrOwJTjkY").build()
        return chain.proceed(request)
    }
}