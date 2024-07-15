package com.android.salamandra._core.domain.model.enums

enum class ExerciseType {
    Strength,
    Hypertrophy,
    Endurance
}

fun Int.toExerciseType() = when(this){
    0 -> ExerciseType.Strength
    1 -> ExerciseType.Hypertrophy
    2 -> ExerciseType.Endurance
    else -> throw IllegalArgumentException("integer to exercise type not mapped")
}