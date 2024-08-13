package com.android.salamandra._core.domain.model.workout.executions

import com.android.salamandra._core.domain.model.Exercise

data class WkExecutionElement(
    val id: String = "",
    val currentRep: Int,
    val reps: Int,
    val weight: Double?,
    val rest: Int = 60,
    val superset: Int? = null,
    val time: Int? = null,
)