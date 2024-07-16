package com.android.salamandra._core.presentation.constants

import androidx.compose.ui.unit.dp


fun wkTemplateLabelWeights(): FloatArray {
    return floatArrayOf(0.5f, 0.1f, 0.1f, 0.15f, 0.1f)
}

object wkTemplateScreenConstants {
    val bannerHeight = 320.dp
    val fixedBannerHeight = 85.dp
    val columnLabelWeights = columnLabelWeights(
        0.5f,
        0.1f,
        0.1f,
        0.15f,
        0.1f
    )
    val sideMargin = 20.dp
    val bannerInBetweenMargin = 20.dp
    val bannerRowWeights = bannerRowWeights(
        180f,
        500f,
        150f,
        180f,
        100f
    )
}

data class columnLabelWeights (
    val exercise: Float,
    val sets: Float,
    val reps: Float,
    val weight: Float,
    val button: Float,
)

data class bannerRowWeights (
    val top: Float,
    val picture: Float,
    val tags: Float,
    val buttons: Float,
    val labels: Float,
)
