package com.android.salamandra._core.presentation.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import com.android.salamandra._core.presentation.UiText

@Composable
fun ErrorDialog(
    error: UiText,
    onDismiss: () -> Unit
) {
    AlertDialog(
        title = { Text(text = "Error") },
        text = { Text(text = error.asString()) },
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text(text = "Ok")
            }
        }
    )
}