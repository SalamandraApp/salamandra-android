package com.android.salamandra._core.data.network

import com.android.salamandra._core.data.network.response.ExerciseResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface SalamandraApiService {
    @GET("exercises")
    suspend fun searchExercise(@Query("name") term: String): ExerciseResponse

}