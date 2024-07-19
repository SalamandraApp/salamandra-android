package com.android.salamandra._core.domain.model.enums

import android.content.Context
import androidx.annotation.StringRes
import androidx.compose.ui.res.stringResource
import com.android.salamandra.R

enum class MuscleGroup(@StringRes val stringId: Int) {
    UpperChest(R.string.upper_chest),
    LowerChest(R.string.lower_chest),
    Chest(R.string.chest),
    FrontDelts(R.string.front_delts),
    MidDelts(R.string.mid_delts),
    RearDelts(R.string.rear_delts),
    Biceps(R.string.biceps),
    Triceps(R.string.triceps),
    Core(R.string.core),
    UpperAbs(R.string.upper_abs),
    LowerAbs(R.string.lower_abs),
    Obliques(R.string.obliques),
    MiddleBack(R.string.middle_back),
    Lats(R.string.lats),
    LowerBack(R.string.lower_back),
    Quadriceps(R.string.quadriceps),
    Hamstrings(R.string.harmstrings),
    Glutes(R.string.glutes),
    Calves(R.string.calves),
}


fun Int.toMuscleGroup() = when (this) {
    0 -> MuscleGroup.UpperChest
    1 -> MuscleGroup.LowerChest
    2 -> MuscleGroup.Chest
    3 -> MuscleGroup.FrontDelts
    4 -> MuscleGroup.MidDelts
    5 -> MuscleGroup.RearDelts
    6 -> MuscleGroup.Biceps
    7 -> MuscleGroup.Triceps
    8 -> MuscleGroup.Core
    9 -> MuscleGroup.UpperAbs
    10 -> MuscleGroup.LowerAbs
    11 -> MuscleGroup.Obliques
    12 -> MuscleGroup.MiddleBack
    13 -> MuscleGroup.Lats
    14 -> MuscleGroup.LowerBack
    15 -> MuscleGroup.Quadriceps
    16 -> MuscleGroup.Hamstrings
    17 -> MuscleGroup.Glutes
    18 -> MuscleGroup.Calves
    else -> throw IllegalArgumentException("Integer to MuscleGroup not mapped")
}