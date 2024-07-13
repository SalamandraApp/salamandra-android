package com.android.salamandra._core.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.android.salamandra.ui.theme.tertiary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheet(
    sheetState: SheetState,
    onDismiss: () -> Unit,
    content: @Composable () -> Unit,
) {
    ModalBottomSheet(
        containerColor = tertiary,
        modifier = Modifier
            .fillMaxHeight(),
        sheetState = sheetState,
        onDismissRequest = { onDismiss() }
    ) {
        Column (
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 25.dp)
        ){
            content()
        }
    }
}