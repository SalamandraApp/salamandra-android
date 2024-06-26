package com.android.salamandra._core.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp

@Composable
fun WkTemplatePicture(
    modifier: Modifier = Modifier,
    size: Int = 0,
    shape: Shape = RectangleShape
) {

    val boxModifier = if (size != 0) {
        modifier.size(size.dp)
    } else {
        modifier
    }

    val iconSize = if (size != 0) {
        (size * 0.6).dp
    } else {
        80.dp
    }
    Box(
        modifier = boxModifier
            .clip(shape)
            .background(Color.DarkGray)
    ) {
        Icon(
            imageVector = Icons.Filled.Image,
            contentDescription = "Image icon",
            tint = Color.Gray.copy(alpha = 0.7f),
            modifier = Modifier
                .align(Alignment.Center)
                .size(iconSize)
        )
    }
}

