package com.android.salamandra._core.domain.model.workout.executions

data class WkExecutionSet(
    private val executionElements: List<WkExecutionElement>,
    private val position: Int
)