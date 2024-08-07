package com.android.salamandra._core.domain.model.workout

import java.time.LocalDate

data class WorkoutTemplate(
    val wkId: String = "",
    val name: String = "New Workout",
    val elements: List<WkTemplateElement> = emptyList(),
    val description: String? = null,
    val dateCreated: LocalDate? = null
)
