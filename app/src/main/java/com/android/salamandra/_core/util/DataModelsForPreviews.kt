package com.android.salamandra._core.util

import com.android.salamandra._core.domain.model.workout.WorkoutPreview
import java.util.UUID

val WORKOUT_PREVIEW = WorkoutPreview(wkId = UUID.randomUUID(), name = "Upper Body Hypertrophy")
val WORKOUT_PREVIEW2 = WorkoutPreview(wkId = UUID.randomUUID(), name = "Legs")

val WORKOUT_PREVIEW_LIST = listOf(WORKOUT_PREVIEW, WORKOUT_PREVIEW2,WORKOUT_PREVIEW, WORKOUT_PREVIEW2)