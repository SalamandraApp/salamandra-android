package com.android.salamandra.ui.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.android.salamandra.R

@Composable
fun MyAlertDialog(
    modifier: Modifier = Modifier,
    title: String,
    text: String,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    showDialog: Boolean
) {
    if (showDialog) {
        AlertDialog(
            modifier = modifier,
            onDismissRequest = { onDismiss() },
            confirmButton = {
                TextButton(
                    onClick = {
                        onConfirm()
                    }
                ) {
                    Text(stringResource(R.string.ok))
                }
            },
            title = { Text(text = title) },
            text = { Text(text = text) })
    }
}
@Composable
fun MyConfirmDialog(
    modifier: Modifier = Modifier,
    title: String,
    text: String,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    showDialog: Boolean
) {
    if (showDialog) {
        AlertDialog(
            modifier = modifier,
            onDismissRequest = { onDismiss() },
            confirmButton = {
                TextButton(
                    onClick = {
                        onConfirm()
                    }
                ) {
                    Text(stringResource(R.string.confirm))
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        onDismiss()
                    }
                ) {
                    Text(stringResource(R.string.cancel))
                }
            },
            title = { Text(text = title) },
            text = { Text(text = text) })
    }
}