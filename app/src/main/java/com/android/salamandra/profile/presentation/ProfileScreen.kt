package com.android.salamandra.profile.presentation

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Construction
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.android.salamandra._core.presentation.components.FadeLip
import com.android.salamandra._core.presentation.components.ProfilePicture
import com.android.salamandra._core.presentation.components.WkTemplatePicture
import com.android.salamandra._core.presentation.components.bottomBar.MyBottomBarScaffold
import com.android.salamandra.destinations.LoginScreenDestination
import com.android.salamandra.destinations.ProfileScreenDestination
import com.android.salamandra.ui.theme.SalamandraTheme
import com.android.salamandra.ui.theme.SemiTypo
import com.android.salamandra.ui.theme.TitleTypo
import com.android.salamandra.ui.theme.onTertiary
import com.android.salamandra.ui.theme.primary
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
            ProfileEvent.NavigateToLogin -> navigator.navigate(LoginScreenDestination)
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

        val infoWeight = 0.6f
        val bannerWeight = 0.4f
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(mainColor),
            verticalArrangement = Arrangement.Top
        ) {
            ProfileBanner(bgColor = mainColor, modifier = Modifier.weight(bannerWeight))
            FadeLip()
            OutlinedButton(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally),
                border = BorderStroke(1.dp, primary),
                onClick = { sendIntent(ProfileIntent.GoToLogin) }) { //TODO change
                Text(text = "Go to login", color = primary)
            }
            Spacer(modifier = Modifier.weight(infoWeight))
        }
    }
}

@Composable
private fun ProfileBanner(
    modifier: Modifier = Modifier,
    bgColor: Color,
) {
    val bannerPicWeight = 0.35f
    val bannerPfpWeight = 0.5f
    val bannerBadgesWeight = 0.15f
    
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(bgColor)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .weight(bannerPicWeight)
        ) {
            WkTemplatePicture(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth()
            )
        }

        val sideMargin = 25.dp
        Row (
            modifier = Modifier
                .fillMaxWidth()
                .weight(bannerPfpWeight),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            val pfpWeight = 0.4f
            val usernameWeight = 0.5f
            val buttonsWeight = 0.1f
            Column (
                modifier = Modifier
                    .weight(pfpWeight)
                    .padding(start = sideMargin - 10.dp)
            ) {
                ProfilePicture(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1f),
                    pad = 10.dp
                )
            }

            Column (
                modifier = Modifier
                    .weight(usernameWeight)
                    .align(Alignment.CenterVertically)
                    .padding(horizontal = 10.dp)
            ){
                Text(
                    text = "Lil Nicolas",
                    color = title,
                    style = TitleTypo,
                    fontSize = 24.sp
                )
                Text(
                    modifier = Modifier.padding(top = 10.dp),
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
            Column (
                modifier = Modifier
                    .weight(buttonsWeight)
                    .align(Alignment.CenterVertically)
                    .padding(end = sideMargin - 10.dp)
            ){
                IconButton(
                    onClick = {/*TODO*/ },
                ) {
                    Icon(
                        modifier = Modifier.size(50.dp),
                        imageVector = Icons.Filled.Construction,
                        tint = onTertiary,
                        contentDescription = "WIP"
                    )
                }
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .weight(bannerBadgesWeight)
                .align(Alignment.Start),
            verticalAlignment = Alignment.CenterVertically
        )
        {
            Spacer(modifier = Modifier.weight(1f))
            IconButton(
                onClick = {/*TODO*/ },
            ) {
                Icon(
                    modifier = Modifier.size(25.dp),
                    imageVector = Icons.Filled.Construction,
                    tint = onTertiary,
                    contentDescription = "WIP"
                )
            }
            Spacer(modifier = Modifier.weight(1f))
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
