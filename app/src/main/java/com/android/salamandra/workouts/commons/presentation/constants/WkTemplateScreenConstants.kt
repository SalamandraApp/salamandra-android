package com.android.salamandra.workouts.commons.presentation.constants

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

object WkTemplateScreenConstants {
    val fixedBannerHeight = 85.dp
    val columnLabelWeights = ColumnLabelWeights(
        0.5f,
        0.1f,
        0.1f,
        0.15f,
        0.1f
    )
    val sideMargin = 20.dp
    val bannerInBetweenMargin = 22.dp
    val bannerRowHeights = BannerRowHeights(
        top     = 50.dp,
        picture = 150.dp,
        tags    = 30.dp,
        buttons = 60.dp,
        labels  = 50.dp
    )
    val bannerHeight: Dp = with(bannerRowHeights) {
        top + picture + tags + buttons + labels
    }
}
data class BannerRowHeights (
    val top: Dp,
    val picture: Dp,
    val tags: Dp,
    val buttons: Dp,
    val labels: Dp,
)

data class ColumnLabelWeights (
    val exercise: Float,
    val sets: Float,
    val reps: Float,
    val weight: Float,
    val button: Float,
)
