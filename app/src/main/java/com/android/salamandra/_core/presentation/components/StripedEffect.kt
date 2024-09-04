package com.android.salamandra._core.presentation.components

import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

fun Modifier.striped(
    color: Color,
    angle: Float = 45f,
    spacing: Dp = 8.dp
): Modifier = this.then(
    Modifier.drawBehind {
        drawLongStripedPattern(color, angle, spacing)
    }
)

private fun DrawScope.drawLongStripedPattern(
    color: Color,
    angle: Float,
    spacing: Dp
) {
    val spacingPx = spacing.toPx()
    val lineCount = (size.width / spacingPx).toInt()

    val radians = Math.toRadians(angle.toDouble())
    val sinAngle = kotlin.math.sin(radians)
    val cosAngle = kotlin.math.cos(radians)

    // Calculate the length that covers the longest diagonal
    val length = kotlin.math.hypot(size.width, size.height) * 2

    for (i in -lineCount..lineCount + 5) {
        val offset = i * spacingPx
        val x1 = (offset * cosAngle - length * sinAngle).toFloat()
        val y1 = (offset * sinAngle + length * cosAngle).toFloat()
        val x2 = (offset * cosAngle + length * sinAngle).toFloat()
        val y2 = (offset * sinAngle - length * cosAngle).toFloat()

        drawLine(
            color = color,
            start = Offset(x1, y1),
            end = Offset(x2, y2),
            strokeWidth = 2.dp.toPx(),
            cap = Stroke.DefaultCap
        )
    }
}


