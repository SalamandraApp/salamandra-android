package com.android.salamandra._core.domain.model.workout.executions

data class WkExecutionElement(
    val id: String = "",
    val setNumber: Int,
    val reps: Int,
    val weight: Double?,
    val rest: Int = 60,
    val superset: Int? = null,
    val time: Int = 0,
)