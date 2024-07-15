package com.android.salamandra._core.domain.model.enums

enum class MuscleGroup {
    Chest,
    Legs,
    Arms,
    Hamstrings,
    Back
}

fun Int.toMuscleGroup() = when (this) {
    0 -> MuscleGroup.Chest
    1 -> MuscleGroup.Legs
    2 -> MuscleGroup.Arms
    3 -> MuscleGroup.Hamstrings
    4 -> MuscleGroup.Back
    else -> throw IllegalArgumentException("integer to Muscle group not mapped")
}
