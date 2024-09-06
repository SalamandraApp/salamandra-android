package com.android.salamandra._core.domain.model.workout.template

import com.android.salamandra._core.domain.model.Exercise
import com.android.salamandra._core.domain.model.workout.executions.WkExecutionElement
import com.android.salamandra._core.domain.model.workout.executions.WkExecutionExercise

data class WkTemplateElement(
    val templateElementId: String = "",
    val exercise: Exercise,
    val position: Int? = null,
    val reps: Int = 1,
    val sets: Int = 1,
    val weight: Double? = 0.0,
    val rest: Int = 60,
    val superset: Int? = null
) {
    fun toWkExecutionExercise(): WkExecutionExercise {
        val executionElements = mutableListOf<WkExecutionElement>()
        for (i in 0..< sets)
            executionElements.add(
                WkExecutionElement(
                    setNumber = i,
                    reps = reps,
                    weight = weight,
                    rest = rest,
                    superset = superset
                )
            )
        return WkExecutionExercise(
            executionElements = executionElements,
            exercise = exercise,
            exerciseNumber = position
                ?: throw IllegalArgumentException("Position is mandatory for a WkTemplateElement")
        )
    }
}