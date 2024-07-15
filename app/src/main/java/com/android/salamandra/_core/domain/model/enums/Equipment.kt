package com.android.salamandra._core.domain.model.enums

enum class Equipment {
    Barbell,
    Dumbbell,
    Machine,
}

fun Int.toEquipment() = when(this){
    0 -> Equipment.Barbell
    1 -> Equipment.Dumbbell
    2 -> Equipment.Machine
    else -> throw IllegalArgumentException("integer to equipment not mapped")
}