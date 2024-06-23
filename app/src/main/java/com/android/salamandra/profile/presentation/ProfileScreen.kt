package com.android.salamandra.profile.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.android.salamandra._core.presentation.components.FadeLip
import com.android.salamandra._core.presentation.components.ProfilePicture
import com.android.salamandra._core.presentation.components.WkPlaceholder
import com.android.salamandra._core.presentation.components.bottomBar.MyBottomBarScaffold
import com.android.salamandra.destinations.ProfileScreenDestination
import com.android.salamandra.ui.theme.SalamandraTheme
import com.android.salamandra.ui.theme.tertiary
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Destination
@Composable
fun ProfileScreen(navigator: DestinationsNavigator, viewModel: ProfileViewModel = hiltViewModel()) {
    val state by viewModel.state.collectAsState()
    val events by viewModel.events.collectAsState(initial = null)
    LaunchedEffect(events) {
        when (events) {
            is ProfileEvent.BottomBarClicked -> navigator.navigate((events as ProfileEvent.BottomBarClicked).destination)
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
    state: ProfileState,
    sendIntent: (ProfileIntent) -> Unit
) {
    MyBottomBarScaffold(
        currentDestination = ProfileScreenDestination,
        onBottomBarClicked = { sendIntent(ProfileIntent.BottomBarClicked(it)) }
    ) {
        val mainColor = tertiary
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(mainColor),
            verticalArrangement = Arrangement.Top
        ) {
            ProfileBanner(backgroundColor = mainColor)
            FadeLip(backgroundColor = mainColor)
        }
    }
}

@Composable
private fun ProfileBanner(
    backgroundColor: Color
) {
    val bannerHeight = 300
    val bannerPicHeight = 150
    val bannerInfoHeight = bannerHeight - bannerPicHeight
    Column (
        modifier = Modifier
            .fillMaxWidth()
            .background(backgroundColor)
            .height(bannerHeight.dp)
    ) {
        Row (
            modifier = Modifier
                .fillMaxWidth()
        ) {

            WkPlaceholder(
                modifier = Modifier.fillMaxWidth(),
                size = bannerPicHeight,
                shape = RoundedCornerShape(0)
            )
            Box(

            ) {
                ProfilePicture(size = 42)
            }
        }
    }
}

@Preview()
@Composable
private fun ScreenPreview() {
    SalamandraTheme {
        ScreenBody(
            state = ProfileState.initial,
            sendIntent = {}
        )
    }
}
