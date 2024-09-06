package com.android.salamandra._core.presentation.components

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
fun GradientSlider() {
    val sliderPosition = remember { mutableFloatStateOf(0f) }

    Column(
        modifier = Modifier
            .padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(32.dp),
            contentAlignment = Alignment.Center
        ) {
            // Draw the gradient for the active track
            Canvas (modifier = Modifier.fillMaxWidth().height(8.dp)) {
                drawRoundRect(
                    brush = Brush.horizontalGradient(
                        listOf(colorConfirm, colorConfirm, primaryVariant, colorError, colorError)
                    ),
                    size = size,
                    cornerRadius = androidx.compose.ui.geometry.CornerRadius(16.dp.toPx())
                )
            }

            // Slider on top of the gradient
            Slider(
                value = sliderPosition.value,
                onValueChange = { sliderPosition.value = it },
                steps = 19,
                valueRange = 0f..300f,
                colors = SliderDefaults.colors(
                    thumbColor = title, // You can adjust the thumb color
                    activeTrackColor = Color.Transparent, // Make active track transparent to show the gradient
                    inactiveTrackColor = Color.Transparent// Customize inactive track color
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 4.dp)
            )
        }
    }
}
