package com.android.salamandra._core.presentation.components

import androidx.compose.foundation.background
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import com.android.salamandra._core.presentation.UiText
import com.android.salamandra.ui.theme.primaryVariant
import com.android.salamandra.ui.theme.secondary

@Composable
fun ErrorDialog(
    error: UiText,
    onDismiss: () -> Unit
) {
    AlertDialog(
        containerColor = secondary,
        title = { Text(text = "Error") },
        text = { Text(text = error.asString()) },
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text(text = "Ok", fontSize = 20.sp)
            }
        }
    )
}