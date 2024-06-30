package com.android.salamandra._core.data.network

import com.android.salamandra._core.data.network.request.CreateUserRequest
import com.android.salamandra._core.data.network.response.CreateUserResponse
import com.android.salamandra._core.data.network.response.ExerciseResponse
import com.android.salamandra._core.data.network.response.WkPreviewsResponse
import com.android.salamandra._core.domain.model.User
import com.android.salamandra._core.domain.model.workout.WorkoutPreview
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface SalamandraApiService {
    @GET("exercises")
    suspend fun searchExercise(@Query("name") term: String): ExerciseResponse

    @GET("users/{userId}/workout-templates")
    suspend fun getWorkoutPreviews(@Path("userId") userId: String): WkPreviewsResponse

    @POST("users")
    suspend fun createUser(@Body user: CreateUserRequest): CreateUserResponse

}