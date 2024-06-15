package com.android.salamandra._core.domain.model.workout

import java.util.Date
import java.util.UUID

data class WorkoutTemplate(
    val wkId: UUID,
    val userId: UUID?,
    val name: String,
    val elements: List<WkTemplateElement>?,
    val description: String?,
    val dateCreated: Date?
)
