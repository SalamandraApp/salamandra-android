package com.android.salamandra._core.domain.model.enums

enum class FitnessGoal {
    Bulking
}

fun Int.toFitnessGoal() = when(this){
    0 -> FitnessGoal.Bulking
    else -> throw IllegalArgumentException("integer to Gender not mapped")
}