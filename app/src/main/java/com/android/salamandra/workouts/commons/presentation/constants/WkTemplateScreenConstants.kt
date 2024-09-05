package com.android.salamandra.workouts.commons.presentation.constants

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

object WkTemplateScreenConstants {
    val columnLabelWeights = ColumnLabelWeights(
        0.5f,
        0.1f,
        0.1f,
        0.15f,
        0.1f
    )
    val outsideMargin = 20.dp
    val bannerRowHeights = BannerRowHeights(
        top     = 20.dp,
        picture = 130.dp,
        tags    = 30.dp,
        buttons = 55.dp,
        labels  = 30.dp,
        margins = listOf(15.dp, 20.dp, 18.dp, 15.dp),
    )
    val bannerHeight: Dp = with(bannerRowHeights) {
        top + picture + tags + buttons + labels + margins[0] + margins[1] + margins[2] + margins[3] + outsideMargin
    }
    val fixedBannerHeight: Dp = with(bannerRowHeights) {
        buttons + labels + outsideMargin + margins[3]
    }
}
data class BannerRowHeights (
    val top: Dp,
    val picture: Dp,
    val tags: Dp,
    val buttons: Dp,
    val labels: Dp,
    val margins: List<Dp>,
)

data class ColumnLabelWeights (
    val exercise: Float,
    val sets: Float,
    val reps: Float,
    val weight: Float,
    val button: Float,
)
