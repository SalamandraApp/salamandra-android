package com.android.salamandra._core.domain.model.enums

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.DirectionsRun
import androidx.compose.material.icons.automirrored.outlined.DirectionsWalk
import androidx.compose.material.icons.outlined.ChildCare
import androidx.compose.material.icons.outlined.DirectionsRun
import androidx.compose.material.icons.outlined.HourglassEmpty
import androidx.compose.material.icons.outlined.MonitorWeight
import androidx.compose.material.icons.outlined.PersonalInjury
import androidx.compose.material.icons.outlined.SportsGymnastics
import androidx.compose.material.icons.outlined.Stairs
import androidx.compose.material.icons.outlined.Start
import androidx.compose.material.icons.outlined.WorkspacePremium
import androidx.compose.ui.graphics.vector.ImageVector
import com.android.salamandra._core.presentation.components.IconShimmer

enum class FitnessLevel {
    Injured,
    TakingABreak,
    GettingStarted,
    SomeExperience,
    Amateur,
    Athlete,
    Elite;

    companion object {
        fun getSize() = FitnessGoal.entries.size
        fun getLowest(): FitnessLevel{
            return FitnessLevel.entries.first()
        }
    }
    override fun toString(): String {
        return when (this) {
            Injured -> "Injured"
            TakingABreak -> "Taking a Break"
            GettingStarted -> "Getting Started"
            SomeExperience -> "Some Experience"
            Amateur -> "Amateur"
            Athlete -> "Athlete"
            Elite -> "Elite"
        }
    }
}

fun FitnessLevel.getIcon(): ImageVector {
    return when (this) {
        FitnessLevel.Injured -> Icons.Outlined.PersonalInjury
        FitnessLevel.TakingABreak -> Icons.Outlined.HourglassEmpty
        FitnessLevel.GettingStarted -> Icons.Outlined.ChildCare
        FitnessLevel.SomeExperience -> Icons.AutoMirrored.Outlined.DirectionsWalk
        FitnessLevel.Amateur -> Icons.AutoMirrored.Outlined.DirectionsRun
        FitnessLevel.Athlete -> Icons.Outlined.SportsGymnastics
        FitnessLevel.Elite -> Icons.Outlined.WorkspacePremium
    }
}
fun Int.toFitnessLevel() = when (this) {
    0 -> FitnessLevel.Injured
    1 -> FitnessLevel.TakingABreak
    2 -> FitnessLevel.GettingStarted
    3 -> FitnessLevel.SomeExperience
    4 -> FitnessLevel.Amateur
    5 -> FitnessLevel.Athlete
    6 -> FitnessLevel.Elite
    else -> throw IllegalArgumentException("Integer to FitnessLevel not mapped")
}

fun FitnessLevel.toInt(): Int {
    return when (this) {
        FitnessLevel.Injured -> 0
        FitnessLevel.TakingABreak -> 1
        FitnessLevel.GettingStarted -> 2
        FitnessLevel.SomeExperience -> 3
        FitnessLevel.Amateur -> 4
        FitnessLevel.Athlete -> 5
        FitnessLevel.Elite -> 6
    }
}