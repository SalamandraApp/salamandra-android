package com.android.salamandra._core.data.network

import com.android.salamandra._core.data.network.response.ExerciseResponse
import com.android.salamandra._core.domain.model.workout.WorkoutPreview
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

interface SalamandraApiService {
    @GET("exercises")
    suspend fun searchExercise(@Query("name") term: String): ExerciseResponse

    @GET("workoutPreviews") //TODO adjust the name of this endpoint
    suspend fun getWorkoutPreviews(): List<WorkoutPreview>

}