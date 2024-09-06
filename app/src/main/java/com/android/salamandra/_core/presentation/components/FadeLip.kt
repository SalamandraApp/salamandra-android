package com.android.salamandra._core.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun FadeLip(
    vertical: Boolean = false,
    reverse: Boolean = false,
    modifier: Modifier = Modifier
) {
    val topColor =      if (reverse) Color.Transparent else Color.Black.copy(alpha = 0.6f)
    val bottomColor =   if (reverse) Color.Black.copy(alpha = 0.6f) else Color.Transparent
    if (vertical) {
        Row(
            modifier = modifier
                .fillMaxHeight()
                .width(6.dp)
                .background(
                    brush = Brush.horizontalGradient(
                        colors = listOf(
                            Color.Transparent,
                            Color.Black.copy(0.6f),
                        )
                    )
                )
        ) {}
    }
    else {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .height(6.dp)
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            topColor,
                            bottomColor,
                        )
                    )
                )
        ) {}
    }

}