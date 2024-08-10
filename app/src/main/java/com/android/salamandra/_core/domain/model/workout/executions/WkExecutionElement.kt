package com.android.salamandra._core.domain.model.workout.executions

import com.android.salamandra._core.domain.model.Exercise

data class WkExecutionElement(
    private val id: String = "",
    private val exercise: Exercise,
    private val setNumber: Int,
    private val reps: Int,
    private val weight: Double?,
    private val rest: Int? = null,
    private val superset: Int? = null,
    private val time: Int? = null,
)