package com.android.salamandra._core.data.network

import com.android.salamandra._core.data.network.response.ExerciseResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface SalamandraApiService {
    @GET("exercises/search/{term}")
    suspend fun searchExercise(@Path("term") term: String): ExerciseResponse

}