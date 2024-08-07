package com.android.salamandra.settings.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.android.salamandra.R
import com.android.salamandra._core.presentation.asUiText
import com.android.salamandra._core.presentation.components.ErrorDialog
import com.android.salamandra.destinations.HomeScreenDestination
import com.android.salamandra.profile.presentation.ProfileIntent
import com.android.salamandra.ui.theme.SalamandraTheme
import com.android.salamandra.ui.theme.colorMessage
import com.android.salamandra.ui.theme.onTertiary
import com.android.salamandra.ui.theme.primaryVariant
import com.android.salamandra.ui.theme.tertiary
import com.android.salamandra.workouts.editWk.presentation.EditWkEvent
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Destination(navArgsDelegate = SettingsNavArgs::class)
@Composable
fun SettingsScreen(navigator: DestinationsNavigator, viewModel: SettingsViewModel = hiltViewModel()) {
    val state by viewModel.state.collectAsState()
    val events by viewModel.events.collectAsState(initial = null)
    LaunchedEffect(events) {
        when (events) {
            SettingsEvent.NavigateUp -> navigator.navigateUp()
            SettingsEvent.NavigateToHome -> navigator.navigate(HomeScreenDestination)
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

        Column(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.weight(1f))
            Button(
                modifier = Modifier
                    .height(80.dp)
                    .padding(horizontal = 50.dp)
                    .fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = primaryVariant,
                ),
                shape = RoundedCornerShape(40),
                onClick = { sendIntent(SettingsIntent.Logout)}
            ) {
                Text(
                    text = "Logout",
                    color = tertiary,
                    fontSize = 20.sp
                )
            }
            IconButton(
                onClick = {sendIntent(SettingsIntent.NavigateUp) },
                modifier = Modifier.padding(10.dp)
            ) {
                Icon(
                    imageVector = Icons.Outlined.Close,
                    tint = onTertiary,
                    contentDescription = "Exit Settings"
                )
            }
            Spacer(modifier = Modifier.weight(1f))
        }


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
