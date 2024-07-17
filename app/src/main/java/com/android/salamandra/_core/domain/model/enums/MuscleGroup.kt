package com.android.salamandra._core.domain.model.enums

import android.content.Context
import androidx.compose.ui.res.stringResource
import com.android.salamandra.R

enum class MuscleGroup(val stringId: Int) {
    Chest(R.string.chest),
    Legs(R.string.legs),
    Arms(R.string.arms),
    Core(R.string.core),
    Back(R.string.core);
}

fun Int.toMuscleGroup() = when (this) {
    0 -> MuscleGroup.Chest
    1 -> MuscleGroup.Legs
    2 -> MuscleGroup.Arms
    3 -> MuscleGroup.Core
    4 -> MuscleGroup.Back
    else -> throw IllegalArgumentException("Integer to MuscleGroup not mapped")
}
