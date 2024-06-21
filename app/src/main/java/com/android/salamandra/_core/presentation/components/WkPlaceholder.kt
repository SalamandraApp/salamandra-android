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
    size: Int,
    shape: Shape = CircleShape
) {
    Box(
        modifier = Modifier
            .size(size.dp)
            .shadow(elevation = 1.dp, shape = shape)
            .background(Color.DarkGray)
            .clip(shape)
    ) {
        Icon (
            imageVector = Icons.Filled.Image, // Material icon
            contentDescription = "Image icon",
            tint = Color.Gray.copy(alpha = 0.7f), // Set the desired color and opacity
            modifier = Modifier
                .align(Alignment.Center)
                .size((size * 0.7).dp) // Adjust the icon size relative to the box size
        )
    }
}
