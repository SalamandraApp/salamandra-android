package com.android.salamandra._core.domain.model.enums

enum class Equipment {
    Barbell
}

fun Int.toEquipment() = when(this){
    0 -> Equipment.Barbell
    else -> throw IllegalArgumentException("integer to Gender not mapped")
}