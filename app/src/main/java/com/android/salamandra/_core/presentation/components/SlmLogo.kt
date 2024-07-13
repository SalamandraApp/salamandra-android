package com.android.salamandra._core.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.android.salamandra.R

@Composable
fun SlmLogo(modifier: Modifier = Modifier) {
    Image(
        painter = painterResource(id = R.drawable.salamander),
        contentDescription = "App Logo",
        modifier = modifier.size(200.dp),
//        colorFilter = ColorFilter.tint(salamandraColor)
    )
}