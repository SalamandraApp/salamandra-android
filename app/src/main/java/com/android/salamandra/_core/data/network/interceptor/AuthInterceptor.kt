package com.android.salamandra._core.data.network.interceptor

import com.android.salamandra._core.domain.DataStoreRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AuthInterceptor @Inject constructor(
    private val datastore: DataStoreRepository
): Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val token = runBlocking {
            datastore.getToken().first()
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