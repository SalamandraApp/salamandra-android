package com.android.salamandra._core.domain.model.workout

import com.android.salamandra._core.domain.model.Exercise
import java.util.UUID

data class WkTemplateElement(
    val exInWkId: UUID,
    val exercise: Exercise,
    val position: Int,
    val reps: Int,
    val sets: Int,
    val weight: Double,
    val rest: Int,
)