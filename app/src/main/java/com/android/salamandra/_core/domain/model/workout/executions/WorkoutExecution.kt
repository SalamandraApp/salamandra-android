package com.android.salamandra._core.domain.model.workout.executions

import java.time.LocalDate

data class WorkoutExecution(
    private val id: String,
    private val date: LocalDate,
    private val survey: Int,
    private val repElements: List<WkExecutionExercise>
)