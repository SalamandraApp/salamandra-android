package com.android.salamandra._core.domain.model.workout.executions

import com.android.salamandra._core.domain.model.Exercise

data class WkExecutionExercise(
    val exercise: Exercise,
    val executionElements: List<WkExecutionElement>,
    val setNumber: Int
)