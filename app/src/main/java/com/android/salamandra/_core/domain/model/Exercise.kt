package com.android.salamandra._core.domain.model

import com.android.salamandra._core.domain.model.enums.Equipment
import com.android.salamandra._core.domain.model.enums.ExerciseType
import com.android.salamandra._core.domain.model.enums.MuscleGroup
import java.util.UUID

data class Exercise(
    val exId: String,
    val name: String,
    val mainMuscleGroup: MuscleGroup,
    val secondaryMuscleGroup: MuscleGroup,
    val necessaryEquipment: Equipment,
    val exerciseType: ExerciseType
)