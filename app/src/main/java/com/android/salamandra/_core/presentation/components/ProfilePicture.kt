package com.android.salamandra._core.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
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
import androidx.compose.ui.unit.dp
import com.android.salamandra.R

@Composable
fun ProfilePicture(
    modifier: Modifier = Modifier,
    size: Int,
    shape: Shape = CircleShape
) {
    Box(
        modifier = modifier
            .size(size.dp)
            .shadow(elevation = 1.dp, shape = shape)
            .background(Color.DarkGray)
            .clip(shape = shape)
    ) {

        Icon(
            imageVector = Icons.Outlined.Person,
            contentDescription = "Default profile icon",
            tint = Color.Gray, // Light gray icon
            modifier = Modifier
                .align(Alignment.Center)
                .size((size * 0.8).dp) // Adjust the icon size relative to the box size
        )
    }
}
