package com.android.salamandra._core.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.android.salamandra.R

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
            .background(Color.Black)
    ) {
        Image(
            modifier = modifier
                .size(size.dp)
                .clip(shape = shape),
            painter = painterResource(id = R.drawable.wk_placeholder),
            contentDescription = "Profile photo"
        )
    }
}