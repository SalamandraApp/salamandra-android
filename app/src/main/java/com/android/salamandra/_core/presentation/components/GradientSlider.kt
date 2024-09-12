package com.android.salamandra._core.presentation.components

import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.android.salamandra.ui.theme.colorConfirm
import com.android.salamandra.ui.theme.colorError
import com.android.salamandra.ui.theme.primaryVariant
import com.android.salamandra.ui.theme.title

@Composable
fun GradientSlider(
    modifier: Modifier = Modifier,
    maxValue: Int,
    minValue: Int = 0,
    steps: Int = maxValue - minValue,
    sliderPosition : Float,
    onChangeValue: (Float) -> Unit,
    gradientColors: List<Color> = listOf(colorConfirm, colorConfirm, primaryVariant, colorError, colorError),
    thumbColor: Color = Color.White
) {
    val minPosition = minValue.toFloat()
    val maxPosition = maxValue.toFloat()

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(32.dp),
        contentAlignment = Alignment.Center
    ) {
        // Draw the gradient for the active track
        Canvas (modifier = Modifier.fillMaxWidth().height(8.dp)) {
            drawRoundRect(
                brush = Brush.horizontalGradient(gradientColors),
                size = size,
                cornerRadius = androidx.compose.ui.geometry.CornerRadius(16.dp.toPx())
            )
        }

        // Slider on top of the gradient
        Slider(
            value = sliderPosition,
            onValueChange = {
                onChangeValue(it)
                            },
            steps = steps,
            valueRange = minPosition..maxPosition,
            colors = SliderDefaults.colors(
                thumbColor = thumbColor,
                activeTrackColor = Color.Transparent,
                inactiveTrackColor = Color.Transparent
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 4.dp)
        )
    }
}
