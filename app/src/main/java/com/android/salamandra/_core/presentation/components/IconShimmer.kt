package com.android.salamandra._core.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun IconShimmer(modifier: Modifier = Modifier){
   Box(modifier = modifier.size(25.dp).background(Color.Gray).shimmerEffect())
}