package com.android.salamandra._core.data.network

import com.android.salamandra._core.data.network.request.CreateUserRequest
import com.android.salamandra._core.data.network.request.CreateWorkoutTemplateRequest
import com.android.salamandra._core.data.network.response.UserResponse
import com.android.salamandra._core.data.network.response.ExerciseResponse
import com.android.salamandra._core.data.network.response.WkPreviewsResponse
import com.android.salamandra._core.data.network.response.WorkoutTemplateResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface SalamandraApiService {
    @GET("exercises")
    suspend fun searchExercise(@Query("name") term: String): ExerciseResponse

    @GET("users/{userId}/workout-templates")
    suspend fun getWorkoutPreviews(@Path("userId") userId: String): WkPreviewsResponse

    @POST("users")
    suspend fun createUser(@Body user: CreateUserRequest): UserResponse

    @GET("users/{userId}")
    suspend fun getUserData(@Path("userId") userId: String): UserResponse

    @GET("/users/{user_id}/workout-templates/{workout_template_id}")
    suspend fun getWorkoutById(
        @Query("full") full: Boolean,
        @Path("user_id") userId: String,
        @Path("workout_template_id") wkId: String): WorkoutTemplateResponse

    @POST("/users/{user_id}/workout-templates")
    suspend fun createWkTemplate(@Path("user_id") userId: String, @Body wkTemplate: CreateWorkoutTemplateRequest): WorkoutTemplateResponse

}