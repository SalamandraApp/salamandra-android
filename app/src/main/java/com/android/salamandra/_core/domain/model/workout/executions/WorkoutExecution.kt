package com.android.salamandra._core.domain.model.workout.executions

import java.time.LocalDate

data class WorkoutExecution(
    val id: String  = "",
    val date: LocalDate,
    val survey: Int?,
    val elements: List<WkExecutionExercise>
)