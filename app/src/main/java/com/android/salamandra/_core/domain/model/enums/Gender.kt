package com.android.salamandra._core.domain.model.enums

enum class Gender {
    Male,
    Female,
    Other,
    Helicopter,
    Grasshopper
}

fun Int.toGender() = when(this){
    0 -> Gender.Male
    1 -> Gender.Female
    2 -> Gender.Other
    3 -> Gender.Helicopter
    4 -> Gender.Grasshopper
    else -> throw IllegalArgumentException("integer to Gender not mapped")
}