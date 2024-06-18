package com.android.salamandra._core.domain.model.workout

import com.android.salamandra._core.domain.model.Exercise
import java.util.UUID

data class WkTemplateElement(
//    val exInWkId: UUID, //I think this uid is not necessary for this model
    val exercise: Exercise,
    val position: Int,
    val reps: Int = 8,
    val sets: Int = 4,
    val weight: Double? = 70.0,
    val rest: Int = 60,
)