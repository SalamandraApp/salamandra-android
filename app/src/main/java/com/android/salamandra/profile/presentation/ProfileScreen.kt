package com.android.salamandra.profile.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.android.salamandra._core.presentation.components.FadeLip
import com.android.salamandra._core.presentation.components.ProfilePicture
import com.android.salamandra._core.presentation.components.WkTemplatePicture
import com.android.salamandra._core.presentation.components.bottomBar.MyBottomBarScaffold
import com.android.salamandra.destinations.ProfileScreenDestination
import com.android.salamandra.ui.theme.SalamandraTheme
import com.android.salamandra.ui.theme.SemiTypo
import com.android.salamandra.ui.theme.TitleTypo
import com.android.salamandra.ui.theme.onTertiary
import com.android.salamandra.ui.theme.tertiary
import com.android.salamandra.ui.theme.title
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
        val signedIn = true
        val mainColor = tertiary
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(mainColor),
            verticalArrangement = Arrangement.Top
        ) {
            if (signedIn) {
                ProfileBanner(bgColor = mainColor, height = 300.dp)
                FadeLip()
            }
        }
    }
}

@Composable
private fun ProfileBanner(
    bgColor: Color,
    height: Dp
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(bgColor)
            .height(height)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.4f)
        ) {
            WkTemplatePicture(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth()
            )
        }

        val pfpSize = 120
        val pfpBorder = 10
        val leftMargin = 15
        Row (
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.4f)
        ) {
            Column (
                modifier = Modifier
                    .weight(0.4f)
                    .padding(start = leftMargin.dp)
                    .align(Alignment.CenterVertically)
            ) {
                ProfilePicture(
                    modifier = Modifier.fillMaxWidth().aspectRatio(1f),
                    pad = 10.dp
                )
            }
            Column (
                modifier = Modifier
                    .weight(0.6f)
                    .align(Alignment.CenterVertically)
                    .padding(start = 20.dp)
            ){
                Text(
                    text = "Lil Nicolas",
                    color = title,
                    style = TitleTypo,
                    fontSize = 24.sp
                )
                Text(
                    text = "@nikki",
                    color = onTertiary,
                    style = SemiTypo,
                    fontSize = 18.sp
                )
                Text(
                    modifier = Modifier.padding(top = 10.dp),
                    text = "Joined 1-1-2010",
                    color = onTertiary,
                    style = SemiTypo,
                    fontSize = 14.sp
                )
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
