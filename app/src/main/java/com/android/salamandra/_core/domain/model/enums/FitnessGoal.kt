package com.android.salamandra._core.domain.model.enums

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.MonitorWeight
import androidx.compose.ui.graphics.vector.ImageVector

enum class FitnessGoal {
    LoseWeight,
    GainWeight,
    BuildStrength,
    BuildMuscleEndurance,
    StayInShape,
    Bulking,
    Cut;

    companion object {
        fun getSize() = entries.size
        fun getLowest(): FitnessGoal {
            return entries.first()
        }
    }
    override fun toString(): String {
        return when (this) {
            LoseWeight -> "Lose Weight"
            GainWeight -> "Gain Weight"
            BuildStrength -> "Build Strength"
            BuildMuscleEndurance -> "Build Muscle Endurance"
            StayInShape -> "Stay in Shape"
            Bulking -> "Bulking"
            Cut -> "Cut"
        }
    }
}

fun FitnessGoal.getIcon(): ImageVector {
    return when (this) {
        FitnessGoal.LoseWeight -> Icons.Outlined.MonitorWeight
        FitnessGoal.GainWeight -> Icons.Outlined.MonitorWeight
        FitnessGoal.BuildStrength -> Icons.Outlined.MonitorWeight
        FitnessGoal.BuildMuscleEndurance -> Icons.Outlined.MonitorWeight
        FitnessGoal.StayInShape -> Icons.Outlined.MonitorWeight
        FitnessGoal.Bulking -> Icons.Outlined.MonitorWeight
        FitnessGoal.Cut -> Icons.Outlined.MonitorWeight
    }
}
fun Int.toFitnessGoal() = when (this) {
    0 -> FitnessGoal.LoseWeight
    1 -> FitnessGoal.GainWeight
    2 -> FitnessGoal.BuildStrength
    3 -> FitnessGoal.BuildMuscleEndurance
    4 -> FitnessGoal.StayInShape
    5 -> FitnessGoal.Bulking
    6 -> FitnessGoal.Cut
    else -> throw IllegalArgumentException("Integer to FitnessGoal not mapped")
}
fun FitnessGoal.toInt(): Int {
    return when (this) {
        FitnessGoal.LoseWeight -> 0
        FitnessGoal.GainWeight -> 1
        FitnessGoal.BuildStrength -> 2
        FitnessGoal.BuildMuscleEndurance -> 3
        FitnessGoal.StayInShape -> 4
        FitnessGoal.Bulking -> 5
        FitnessGoal.Cut -> 6
    }
}
