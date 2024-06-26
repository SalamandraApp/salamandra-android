package com.android.salamandra._core.domain.model.workout

import java.util.UUID

data class WorkoutPreview (
    val wkId: String,
    val name: String,
    val onlyPreviewAvailable: Boolean = true
)