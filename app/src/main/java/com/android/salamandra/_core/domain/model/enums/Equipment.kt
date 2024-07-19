package com.android.salamandra._core.domain.model.enums

import androidx.annotation.StringRes
import com.android.salamandra.R

enum class Equipment(@StringRes val stringId: Int) {
    Nothing(R.string.nothing),
    Barbell(R.string.barbell),
    Dumbbell(R.string.dumbbell),
    Kettlebell(R.string.kettlebell),
    Bands(R.string.bands),
    Machine(R.string.machine),
    PullUpBar(R.string.pull_up_bar),
    ParallelBar(R.string.parallel_bar),
    Landmine(R.string.landmine),
    HexBar(R.string.hex_bar),
    Weights(R.string.weights),
    WeightBelt(R.string.weight_belt)
}

fun Int.toEquipment() = when (this) {
    0 -> Equipment.Nothing
    1 -> Equipment.Barbell
    2 -> Equipment.Dumbbell
    3 -> Equipment.Kettlebell
    4 -> Equipment.Bands
    5 -> Equipment.Machine
    6 -> Equipment.PullUpBar
    7 -> Equipment.ParallelBar
    8 -> Equipment.Landmine
    9 -> Equipment.HexBar
    10 -> Equipment.Weights
    11 -> Equipment.WeightBelt
    else -> throw IllegalArgumentException("Integer to Equipment not mapped")
}
