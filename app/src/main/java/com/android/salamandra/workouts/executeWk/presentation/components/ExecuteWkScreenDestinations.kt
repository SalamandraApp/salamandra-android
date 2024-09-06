package com.android.salamandra.workouts.executeWk.presentation.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.List
import androidx.compose.material.icons.outlined.Timer
import androidx.compose.ui.graphics.vector.ImageVector

enum class ExecuteWkScreenDestinations(
    val icon: ImageVector,
    val label: String //TODO change to string res
) {
    ExecuteScreen(
        icon = Icons.Outlined.Timer,
        label = "Set"
    ),
    InfoScreen(
        icon = Icons.AutoMirrored.Outlined.List,
        label = "Info"
    ),
}