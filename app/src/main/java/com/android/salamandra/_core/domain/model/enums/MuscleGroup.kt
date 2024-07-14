package com.android.salamandra._core.domain.model.enums

enum class MuscleGroup {
    Chest
}

fun Int.toMuscleGroup() = when (this) {
    0 -> MuscleGroup.Chest
    else -> throw IllegalArgumentException("integer to Muscle group not mapped")
}
