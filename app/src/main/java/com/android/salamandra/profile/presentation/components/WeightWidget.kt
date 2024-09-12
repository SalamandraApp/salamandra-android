package com.android.salamandra.profile.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.outlined.Scale
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.android.salamandra.R
import com.android.salamandra._core.presentation.components.AnimatedFloatingButton
import com.android.salamandra._core.presentation.components.EditWeight
import com.android.salamandra.ui.theme.SemiTypo
import com.android.salamandra.ui.theme.onSecondary
import com.android.salamandra.ui.theme.primaryVariant

@Composable
fun weightWidget(
    textColor: Color,
    iconColor: Color,
    weight: Double?,
    editWeight: Boolean,
    newWeight: Double?,
    onEditWeight: (Double) -> Unit,
    onSaveWeight: () -> Unit,
    onWeight: () -> Unit
) {
    val weightAnnotatedString = buildAnnotatedString {
        append("Weight: ")
        withStyle(style = SpanStyle(color = textColor.copy(alpha = 0.5f))) {
            append(weight?.toString() ?: "???")
            append(" kg")
        }
    }
    if (editWeight && newWeight != null) {
        Column(
            Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val focusRequester = remember { FocusRequester() }
            val keyboardController = LocalSoftwareKeyboardController.current
            val weightKg = buildAnnotatedString {
                append(stringResource(R.string.weight))
                withStyle(style = SpanStyle(color = textColor.copy(alpha = 0.5f))) {
                    append(" kg")
                }
            }
            Text(
                modifier = Modifier.padding(top = 10.dp),
                text = weightKg,
                color = textColor,
                style = SemiTypo,
                fontSize = 16.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(Modifier.weight(1f))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(Modifier.weight(1f)) {
                    EditWeight(
                        modifier = Modifier.focusRequester(focusRequester),
                        weight = newWeight,
                        onEditWeight = onEditWeight
                    )
                }
                LaunchedEffect(Unit) {
                    focusRequester.requestFocus()
                    keyboardController?.show()
                }
                AnimatedFloatingButton(
                    modifier = Modifier.weight(0.8f).padding(start = 10.dp),
                    initialIcon = Icons.Filled.CheckCircle,
                    pressedIcon = Icons.Filled.CheckCircle,
                    delayTime = 700,
                    initialContainerColor = onSecondary.copy(0.2f),
                    pressedContainerColor = primaryVariant.copy(0.3f),
                    initialIconColor = onSecondary,
                    pressedIconColor = primaryVariant,
                    onPress = {onSaveWeight()},
                    instant = false
                )
            }
        }
    } else {
        Column(
            Modifier.fillMaxSize().clickable { onWeight() },
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                modifier = Modifier.size(60.dp),
                imageVector = Icons.Outlined.Scale,
                tint = iconColor,
                contentDescription = "WIP"
            )
            Spacer(Modifier.weight(1f))
            Text(
                modifier = Modifier.padding(top = 10.dp),
                text = weightAnnotatedString,
                color = textColor,
                style = SemiTypo,
                fontSize = 16.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}
