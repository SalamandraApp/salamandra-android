package com.android.salamandra.data.network

import com.android.salamandra.data.network.request.AccessTokenRequest
import com.android.salamandra.data.network.request.AuthRequest
import com.android.salamandra.data.network.response.AccessTokenResponse
import com.android.salamandra.data.network.response.AuthResponse
import com.android.salamandra.data.network.response.ExerciseResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path

interface SalamandraApiService {

    @Headers("Content-Type: application/json")
    @POST("auth/login")
    fun login(@Body requestBody: AuthRequest): Call<AuthResponse>

    @Headers("Content-Type: application/json")
    @POST("auth/register")
    fun register(@Body requestBody: AuthRequest): Call<AuthResponse>

    @GET("exercises/search/{term}")
    suspend fun searchExercise(@Path("term") term: String): ExerciseResponse



    @Headers("Content-Type: application/x-www-form-urlencoded")
    @POST("protocol/openid-connect/token")
    fun getAccessToken(@Body requestBody: AccessTokenRequest): Call<AccessTokenResponse>


}