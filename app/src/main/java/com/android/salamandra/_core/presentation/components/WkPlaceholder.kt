package com.android.salamandra._core.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp

@Composable
fun WkPlaceholder(
    modifier: Modifier = Modifier,
    size: Int? = null,
    size_x: Int? = null,
    shape: Shape = CircleShape
) {
    val width = size_x ?: size ?: 0
    val height = size ?: 0
    val iconSize = 0.7f * minOf(height, width)

    Box(
        modifier = if (size != null && size_x != null) {
            modifier.size(width.dp, height.dp)
        } else {
            modifier
        }
            .shadow(elevation = 1.dp, shape = shape)
            .background(Color.DarkGray)
            .clip(shape)
    ) {
        Icon(
            imageVector = Icons.Filled.Image,
            contentDescription = "Image icon",
            tint = Color.Gray.copy(alpha = 0.7f),
            modifier = Modifier
                .align(Alignment.Center)
                .size(iconSize.dp)
        )
    }
}

