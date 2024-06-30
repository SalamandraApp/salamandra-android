package com.android.salamandra.settings.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.android.salamandra._core.presentation.asUiText
import com.android.salamandra._core.presentation.components.ErrorDialog
import com.android.salamandra.ui.theme.SalamandraTheme
import com.android.salamandra.ui.theme.tertiary
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Destination(navArgsDelegate = SettingsNavArgs::class)
@Composable
fun SettingsScreen(navigator: DestinationsNavigator, viewModel: SettingsViewModel = hiltViewModel()) {
    val state by viewModel.state.collectAsState()
    val events by viewModel.events.collectAsState(initial = null)
    LaunchedEffect(events) {
        TODO()
        when (events) {
            null -> {}
        }
    }

    ScreenBody(
        state = state,
        sendIntent = viewModel::dispatch
    )
}

@Composable
private fun ScreenBody(
    state: SettingsState,
    sendIntent: (SettingsIntent) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(tertiary)
            .padding(12.dp),
        contentAlignment = Alignment.Center
    ) {
        //TODO


        if (state.error != null)
            ErrorDialog(
                error = state.error.asUiText(),
                onDismiss = { sendIntent(SettingsIntent.CloseError) }
            )

    }
}

@Preview()
@Composable
private fun ScreenPreview() {
    SalamandraTheme {
        ScreenBody(
            state = SettingsState.initial,
            sendIntent = {}
        )
    }
}
