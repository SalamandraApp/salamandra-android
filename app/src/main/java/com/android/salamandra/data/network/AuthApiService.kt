package com.android.salamandra.data.network

import com.android.salamandra.data.network.request.LoginRequest
import com.android.salamandra.data.network.request.RegisterRequest
import com.android.salamandra.data.network.response.LoginResponse
import com.android.salamandra.data.network.response.RegisterResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface AuthApiService {

    @Headers("Content-Type: application/json")
    @POST("auth/login")
    fun login(@Body requestBody: LoginRequest): Call<LoginResponse>
    @Headers("Content-Type: application/json")
    @POST("auth/register")
    fun register(@Body requestBody: RegisterRequest): Call<RegisterResponse>
}