package com.android.salamandra.domain.model

import java.util.Date
import java.util.UUID

data class WorkoutTemplate(
    val wkId: UUID,
    val userId: UUID,
    val name: String,
    val elements: List<WKTemplateElement>,
    val description: String,
    val dateCreated: Date
)
