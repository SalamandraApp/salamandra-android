package com.android.salamandra.core.boilerplate.template

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.android.salamandra.ui.theme.SalamandraTheme
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Destination
@Composable
fun TScreen(navigator: DestinationsNavigator, tViewModel: tViewModel = hiltViewModel()) {
    ScreenBody(
        state = tViewModel.state,
        sendIntent = {}
    )
}

@Composable
private fun ScreenBody(
    state: tState,
    sendIntent: (tIntent) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        //TODO


        if (state.error != null) {
            AlertDialog(
                title = { Text(text = "Error") },
                text = { Text(text = state.error.asString()) },
                onDismissRequest = { },
                confirmButton = { })
        }

    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun LightPreview() {
    SalamandraTheme {
        ScreenBody(
           state = tState.initial,
            sendIntent = {}
        )
    }
}
