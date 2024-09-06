package com.android.salamandra.workouts.executeWk.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.android.salamandra._core.domain.model.workout.executions.WkExecutionExercise
import com.android.salamandra._core.presentation.components.striped
import com.android.salamandra.ui.theme.primary
import com.android.salamandra.ui.theme.primaryVariant
import com.android.salamandra.ui.theme.secondaryVariant
import com.android.salamandra.ui.theme.title

@Composable
fun ProgressBar(
    modifier: Modifier = Modifier,
    size: Int,
    current: Int,
    spacer: Dp = 4.dp
) {
    if (current !in 0 until size) {
        throw IllegalArgumentException("Current is out of the valid range: 0 <= current < $size")
    }

    Row(
        modifier = modifier
            .clip(RoundedCornerShape(50))
    ) {
        for (i in 0..< size) {
            val color =
                if (i <= current) primaryVariant else secondaryVariant
            Box(
                if (i != current) {
                    Modifier
                        .fillMaxHeight()
                        .background(color)
                        .weight(1f)
                } else {
                    Modifier
                        .fillMaxHeight()
                        .clip(RectangleShape)
                        .striped(color)
                        .weight(1f)
                }
            )
            if (i < size - 1) {
                Spacer(Modifier.size(spacer))
            }
        }
    }
}
