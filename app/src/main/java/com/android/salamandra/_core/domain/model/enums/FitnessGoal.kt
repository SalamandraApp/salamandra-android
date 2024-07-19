package com.android.salamandra._core.domain.model.enums

enum class FitnessGoal {
    LoseWeight,
    GainWeight,
    BuildStrength,
    BuildMuscleEndurance,
    StayInShape,
    Bulking,
    Cut
}

fun Int.toFitnessGoal() = when (this) {
    0 -> FitnessGoal.LoseWeight
    1 -> FitnessGoal.GainWeight
    2 -> FitnessGoal.BuildStrength
    3 -> FitnessGoal.BuildMuscleEndurance
    4 -> FitnessGoal.StayInShape
    5 -> FitnessGoal.Bulking
    6 -> FitnessGoal.Cut
    else -> throw IllegalArgumentException("Integer to FitnessGoal not mapped")
}