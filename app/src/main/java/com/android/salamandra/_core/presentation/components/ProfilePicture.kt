package com.android.salamandra._core.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Person
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.android.salamandra.R
import com.android.salamandra.ui.theme.secondary


@Composable
fun ProfilePicture(
    modifier: Modifier = Modifier,
    size: Int = 0,
    shape: Shape = CircleShape,
    bgColor: Color = secondary,
    pad: Dp = 0.dp
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
            .padding(pad)
            .clip(shape)
            .background(bgColor)
    ) {
        Icon(
            imageVector = Icons.Outlined.Person,
            contentDescription = "Default profile icon",
            tint = Color.Gray, // Light gray icon
            modifier = Modifier
                .align(Alignment.Center)
                .size(iconSize) // Adjust the icon size based on the condition
        )
    }
}



