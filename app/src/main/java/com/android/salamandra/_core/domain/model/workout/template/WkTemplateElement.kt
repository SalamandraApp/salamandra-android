package com.android.salamandra._core.domain.model.workout.template

import com.android.salamandra._core.domain.model.Exercise

data class WkTemplateElement(
    val templateElementId: String = "",
    val exercise: Exercise,
    val position: Int? = null,
    val reps: Int = 1,
    val sets: Int = 1,
    val weight: Double? = 0.0,
    val rest: Int = 0,
    val superSet: Int? = null
)