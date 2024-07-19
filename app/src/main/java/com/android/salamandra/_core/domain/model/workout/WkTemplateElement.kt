package com.android.salamandra._core.domain.model.workout

import com.android.salamandra._core.domain.model.Exercise
import java.util.UUID

data class WkTemplateElement(
    val templateElementId: String = "",
    val exercise: Exercise,
    val position: Int,
    val reps: Int = 1,
    val sets: Int = 1,
    val weight: Double? = 0.0,
    val rest: Int = 0,
    val superSet: Int? = null
)