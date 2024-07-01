package com.android.salamandra._core.domain.model.workout

import java.util.Date
import java.util.UUID

data class WorkoutTemplate(
    val wkId: String = "",
    val name: String = "Workout #5",
    val elements: List<WkTemplateElement> = emptyList(),
    val description: String? = null,
    val dateCreated: Date? = null
)
