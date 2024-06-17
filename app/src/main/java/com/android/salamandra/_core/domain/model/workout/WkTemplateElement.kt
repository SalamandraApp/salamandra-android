package com.android.salamandra._core.domain.model.workout

import com.android.salamandra._core.domain.model.Exercise
import java.util.UUID

data class WkTemplateElement(
    val exInWkId: UUID,
    val exercise: Exercise,
    val position: Int,
    val reps: Int = 8,
    val sets: Int = 4,
    val weight: Double,
    val rest: Int = 60,
)