package com.android.salamandra.ui.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.android.salamandra.ui.UiText

@Composable
fun ErrorDialog(error: UiText) {
    AlertDialog(
        title = { Text(text = "Error") },
        text = { Text(text = error.asString()) },
        onDismissRequest = { },
        confirmButton = { }
    )
}