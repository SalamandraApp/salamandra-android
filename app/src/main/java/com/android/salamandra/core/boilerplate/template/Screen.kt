package com.android.salamandra.core.boilerplate.template

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.android.salamandra.domain.model.UiError
import com.android.salamandra.ui.components.MyAlertDialog
import com.android.salamandra.ui.theme.SalamandraTheme
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Destination
@Composable
fun TScreen(navigator: DestinationsNavigator, tViewModel: tViewModel = hiltViewModel()) {
    val error = tViewModel.state.error
    ScreenBody(
        error = error,
        onCloseDialog = {
            tViewModel.onCloseDialog()
        }
    )
}

@Composable
private fun ScreenBody(
    error: UiError,
    onCloseDialog: () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        //TODO


        MyAlertDialog(
            title = "Error",
            text = error.errorMsg.orEmpty(),
            onDismiss = { onCloseDialog() },
            onConfirm = { onCloseDialog() },
            showDialog = error.isError
        )
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun LightPreview() {
    SalamandraTheme {
        ScreenBody(
            error = UiError(false, "Account wasn't created"),
            onCloseDialog = {},
        )
    }

}
