package com.android.salamandra._core.domain.model.workout.template

data class WorkoutPreview (
    val wkId: String,
    val name: String,
    val onlyPreviewAvailable: Boolean = true
)