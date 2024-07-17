package com.android.salamandra._core.domain.model.enums

import com.android.salamandra.R

enum class ExerciseType(val stringId: Int) {
    Calisthenics(R.string.calisthenics),
    FreeWeights(R.string.free_weights),
    WarmUp(R.string.warm_up)
}

fun Int.toExerciseType() = when(this){
    0 -> ExerciseType.Calisthenics
    1 -> ExerciseType.FreeWeights
    2 -> ExerciseType.WarmUp
    else -> throw IllegalArgumentException("Integer to ExerciseType not mapped")
}