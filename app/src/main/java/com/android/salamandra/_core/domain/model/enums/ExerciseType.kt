package com.android.salamandra._core.domain.model.enums

import androidx.annotation.StringRes
import com.android.salamandra.R

enum class ExerciseType(@StringRes val stringId: Int) {
    Calisthenics(R.string.calisthenics),
    FreeWeights(R.string.free_weights),
    MachineMovement(R.string.machine_movement),
    Cardio(R.string.cardio),
    Mobility(R.string.mobility)
}

fun Int.toExerciseType() = when (this) {
    0 -> ExerciseType.Calisthenics
    1 -> ExerciseType.FreeWeights
    2 -> ExerciseType.MachineMovement
    3 -> ExerciseType.Cardio
    4 -> ExerciseType.Mobility
    else -> throw IllegalArgumentException("Integer to ExerciseType not mapped")
}
