package com.android.salamandra._core.domain.model.enums

enum class Gender {
    Male,
    Female,
    Other
}

fun Int.toGender() = when(this){
    0 -> Gender.Male
    1 -> Gender.Female
    2 -> Gender.Other
    else -> throw IllegalArgumentException("Integer to Gender not mapped")
}