package com.android.salamandra._core.domain.model.enums

enum class FitnessLevel {
    Beginner
}

fun Int.toFitnessLevel() = when(this){
    0 -> FitnessLevel.Beginner
    else -> throw IllegalArgumentException("Integer to FitnessLevel not mapped")
}