package com.android.salamandra._core.domain.model.enums

import androidx.annotation.StringRes
import com.android.salamandra.R

enum class Equipment(@StringRes val stringId: Int) {
    Barbell(R.string.barbell),
    Dumbbell(R.string.dumbbell),
    Machine(R.string.machine),
}
fun Int.toEquipment() = when(this){
    0 -> Equipment.Barbell
    1 -> Equipment.Dumbbell
    2 -> Equipment.Machine
    else -> throw IllegalArgumentException("Integer to Equipment not mapped")
}