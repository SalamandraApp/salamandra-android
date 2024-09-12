package com.android.salamandra._core.presentation.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.android.salamandra.ui.theme.primaryVariant
import kotlinx.coroutines.delay

@Composable
fun AnimatedFloatingButton(
    modifier: Modifier = Modifier,
    onPress: () -> Unit,
    delayTime: Long,
    initialIcon: ImageVector,
    pressedIcon: ImageVector,
    initialContainerColor: Color,
    pressedContainerColor: Color,
    initialIconColor: Color,
    pressedIconColor: Color,
    instant: Boolean = true,
) {
    var isPressed by remember { mutableStateOf(false) }

    LaunchedEffect(isPressed) {
        if (isPressed) {
            delay(delayTime) // Duration to show the pressed icon
            if (!instant) onPress()
            isPressed = false
        }
    }

    FloatingActionButton(
        modifier = modifier,
        containerColor = if (isPressed) pressedContainerColor else initialContainerColor,
        contentColor = if (isPressed) pressedIconColor else initialIconColor,
        elevation = FloatingActionButtonDefaults.elevation(0.dp),
        onClick = {
            if (instant) {
                onPress()
            }
            isPressed = true
        }) {
        Icon(
            imageVector = if (isPressed) pressedIcon else initialIcon,
            contentDescription = null,
        )

    }
}
