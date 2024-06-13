package com.android.salamandra.domain.model

import java.util.UUID

data class WKTemplateElement(
    val exInWkId: UUID,
    val exercise: Exercise,
    val position: Int,
    val reps: Int,
    val sets: Int,
    val weight: Double,
    val rest: Int,
)