package com.android.salamandra._core.presentation.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.size
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

@Composable
fun AnimatedIconButton(
    onPress: () -> Unit,
    delayTime: Long,
    modifier: Modifier = Modifier,
    initialIcon: ImageVector,
    pressedIcon: ImageVector,
    contentDescription: String? = null,
    tint: Color
) {
    var isPressed by remember { mutableStateOf(false) }

    LaunchedEffect(isPressed) {
        if (isPressed) {
            delay(delayTime) // Duration to show the pressed icon
            isPressed = false
        }
    }

    IconButton(
        onClick = {
            onPress()
            isPressed = true
        },
        modifier = modifier
    ) {
        val scale by animateFloatAsState(if (isPressed) 1.1f else 1f) // Simple scale animation

        Icon(
            imageVector = if (isPressed) pressedIcon else initialIcon,
            contentDescription = contentDescription,
            tint = tint,
            modifier = Modifier.scale(scale).size(24.dp)
        )
    }
}
