package com.android.salamandra._core.domain.model.workout

import java.util.Date
import java.util.UUID

data class WorkoutTemplate(
    val wkId: String = "",
    val name: String = "New workout",
    val elements: List<WkTemplateElement> = emptyList(),
    val description: String? = null,
    val dateCreated: Date? = null
)
