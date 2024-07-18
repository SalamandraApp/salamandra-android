package com.android.salamandra.workouts.editWk.domain

import com.android.salamandra._core.domain.error.DataError
import com.android.salamandra._core.domain.error.Result
import com.android.salamandra._core.domain.model.Exercise
import com.android.salamandra._core.domain.model.workout.WkTemplateElement
import com.android.salamandra._core.domain.model.workout.WorkoutTemplate

interface Repository {
    suspend fun getAllExercises(exerciseIdList: Array<String>): List<Exercise>
    suspend fun createWorkout(workoutTemplate: WorkoutTemplate): Result<Unit, DataError>
    suspend fun retrieveSavedWorkoutTemplateElements(): List<WkTemplateElement>
    suspend fun saveWorkoutTemplateElementsTemporarly(wkTemplateElementList: List<WkTemplateElement>)
    suspend fun deleteTemporalTemplateElements()
}