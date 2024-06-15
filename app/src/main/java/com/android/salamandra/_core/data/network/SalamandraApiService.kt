package com.android.salamandra._core.data.network

import com.android.salamandra._core.data.network.request.AuthRequest
import com.android.salamandra._core.data.network.response.AccessTokenResponse
import com.android.salamandra._core.data.network.response.AuthResponse
import com.android.salamandra._core.data.network.response.ExerciseResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
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

    @FormUrlEncoded
    @POST("protocol/openid-connect/token")
//    fun getAccessToken(@Field("grant_type") grantType: String, @Field("client_id") clientId: String, @Field("client_secret") clientSecret: String): Call<AccessTokenResponse>
    suspend fun getAccessToken(@Field("grant_type") grantType: String, @Field("client_id") clientId: String, @Field("client_secret") clientSecret: String): AccessTokenResponse

}