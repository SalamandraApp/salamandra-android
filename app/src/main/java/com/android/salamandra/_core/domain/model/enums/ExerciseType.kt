package com.android.salamandra._core.domain.model.enums

enum class ExerciseType {
    Strength

}

fun Int.toExerciseType() = when(this){
    0 -> ExerciseType.Strength
    else -> throw IllegalArgumentException("integer to exercise type not mapped")
}