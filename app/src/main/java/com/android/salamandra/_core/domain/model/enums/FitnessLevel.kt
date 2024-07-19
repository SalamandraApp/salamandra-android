package com.android.salamandra._core.domain.model.enums

enum class FitnessLevel {
    Injured,
    TakingABreak,
    GettingStarted,
    SomeExperience,
    Amateur,
    Athlete,
    Elite
}

fun Int.toFitnessLevel() = when (this) {
    0 -> FitnessLevel.Injured
    1 -> FitnessLevel.TakingABreak
    2 -> FitnessLevel.GettingStarted
    3 -> FitnessLevel.SomeExperience
    4 -> FitnessLevel.Amateur
    5 -> FitnessLevel.Athlete
    6 -> FitnessLevel.Elite
    else -> throw IllegalArgumentException("Integer to FitnessLevel not mapped")
}