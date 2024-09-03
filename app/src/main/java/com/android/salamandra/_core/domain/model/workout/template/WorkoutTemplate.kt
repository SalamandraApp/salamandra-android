package com.android.salamandra._core.domain.model.workout.template

import java.time.LocalDate

data class WorkoutTemplate(
    val wkId: String = "",
    val name: String = "Workout",
    val elements: List<WkTemplateElement> = emptyList(),
    val description: String? = "",
    val dateCreated: LocalDate? = null
)
