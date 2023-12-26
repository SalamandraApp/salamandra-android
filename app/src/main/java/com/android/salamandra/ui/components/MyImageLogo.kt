package com.android.salamandra.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.android.salamandra.R
import com.android.salamandra.ui.theme.salamandraColor

@Composable
fun MyImageLogo(modifier: Modifier = Modifier) {
    Image(
        painter = painterResource(id = R.drawable.salamander),
        contentDescription = "App Logo",
        modifier = modifier.size(200.dp),
//        colorFilter = ColorFilter.tint(salamandraColor)
    )
}