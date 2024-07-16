package com.android.salamandra.profile.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.MilitaryTech
import androidx.compose.material.icons.filled.QueryStats
import androidx.compose.material.icons.filled.Settings
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.android.salamandra.R
import com.android.salamandra._core.presentation.components.FadeLip
import com.android.salamandra._core.presentation.components.ProfilePicture
import com.android.salamandra._core.presentation.components.WkTemplatePicture
import com.android.salamandra._core.presentation.components.bottomBar.MyBottomBarScaffold
import com.android.salamandra.destinations.LoginScreenDestination
import com.android.salamandra.destinations.ProfileScreenDestination
import com.android.salamandra.destinations.SettingsScreenDestination
import com.android.salamandra.ui.theme.SalamandraTheme
import com.android.salamandra.ui.theme.SemiTypo
import com.android.salamandra.ui.theme.TitleTypo
import com.android.salamandra.ui.theme.colorMessage
import com.android.salamandra.ui.theme.onSecondary
import com.android.salamandra.ui.theme.onTertiary
import com.android.salamandra.ui.theme.primaryVariant
import com.android.salamandra.ui.theme.secondary
import com.android.salamandra.ui.theme.subtitle
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
            ProfileEvent.NavigateToSettings -> navigator.navigate(SettingsScreenDestination())
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

        val infoWeight = 0.6f
        val bannerWeight = 0.4f
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(mainColor),
            verticalArrangement = Arrangement.Top
        ) {
            ProfileBanner(
                state = state,
                sendIntent = sendIntent,
                bgColor = mainColor,
                modifier = Modifier.weight(bannerWeight)
            )
            FadeLip()
            InfoSection(
                modifier = Modifier
                    .weight(infoWeight),
                sendIntent = sendIntent,
                state = state,
                bgColor = tertiary
            )
        }
        if (!state.isSignedIn) {
            NotLoggedInCover ( sendIntent = sendIntent )
        }

    }
}
@Composable
private fun NotLoggedInCover(sendIntent: (ProfileIntent) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(tertiary.copy(alpha = 0.95f))
            .padding(horizontal = 60.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val buttonWeight = 70f
        val spacerWeights = 1000f - buttonWeight

        Spacer(modifier = Modifier.weight(spacerWeights/2))
        Text(
            modifier = Modifier.padding(bottom = 18.dp),
            text = stringResource(R.string.need_to_login),
            color = primaryVariant,
            fontSize = 16.sp,
        )
        Button(
            modifier = Modifier
                .weight(buttonWeight)
                .fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = primaryVariant,
            ),
            shape = RoundedCornerShape(40),
            onClick = { sendIntent(ProfileIntent.GoToLogin) }
        ) {
            Text(
                text = stringResource(R.string.login),
                color = tertiary,
                fontSize = 20.sp
            )
        }
        Spacer(modifier = Modifier.weight(spacerWeights/2))
    }
}

@Composable
private fun InfoSection (
    modifier: Modifier = Modifier,
    bgColor: Color,
    sendIntent: (ProfileIntent) -> Unit,
    state: ProfileState
) {
    val dpSideMargin = 20.dp
    val dpVerticalPadding = 20.dp
    LazyColumn (
        modifier = modifier.fillMaxWidth(),
    ){
        item {
            BasicInfo(
                modifier = Modifier
                    .height(300.dp)
                    .fillMaxWidth()
                    .padding(vertical = dpVerticalPadding, horizontal = dpSideMargin),
                sendIntent = sendIntent,
                state = state,
            )
        }
    }
}

@Composable
private fun BasicInfo (
    modifier: Modifier = Modifier,
    sendIntent: (ProfileIntent) -> Unit,
    state: ProfileState
) {
    val dpInsidePadding = 10.dp
    val dpBoxMargin = 10.dp
    val iconColor = onSecondary
    val textColor = subtitle

    val weightAnnotatedString = buildAnnotatedString {
        append("Weight: ")
        withStyle(style = SpanStyle(color = textColor.copy(alpha = 0.5f))) {
            append(state.userData?.weight?.toString()?: "???")
            append(" kg")
        }
    }

    val fitnessLvlAnnotatedString = buildAnnotatedString {
        append("Fitness Level: ")
        withStyle(style = SpanStyle(color = textColor.copy(alpha = 0.5f))) {
            append(state.userData?.fitnessLevel?.toString()?: "Beginner")
        }
    }

    val fitnessGoalAnnotatedString = buildAnnotatedString {
        append("Fitness Goal: ")
        withStyle(style = SpanStyle(color = textColor.copy(alpha = 0.5f))) {
            append(state.userData?.fitnessGoal?.toString()?: "Get in shape")
        }
    }
    Row (
        modifier = modifier
    ) {
        Column (
            modifier = Modifier
                .fillMaxSize()
        ){
            Row (
                modifier = Modifier
                    .padding(bottom = dpInsidePadding)
                    .weight(1f)
            ){
                Column (
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = dpInsidePadding / 2)
                        .fillMaxHeight()
                        .clip(RoundedCornerShape(8.dp))
                        .background(secondary)
                        .padding(dpBoxMargin),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center

                ) {
                    Icon(
                        modifier = Modifier.size(60.dp),
                        imageVector = Icons.Filled.QueryStats,
                        tint = iconColor,
                        contentDescription = "WIP"
                    )

                    Text(
                        modifier = Modifier.padding(top = 10.dp),
                        text = weightAnnotatedString,
                        color = textColor,
                        style = SemiTypo,
                        fontSize = 16.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                Column (
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = dpInsidePadding / 2)
                        .fillMaxHeight()
                        .clip(RoundedCornerShape(8.dp))
                        .background(secondary)
                        .padding(dpBoxMargin),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        modifier = Modifier.size(60.dp),
                        imageVector = Icons.Filled.FitnessCenter,
                        tint = iconColor,
                        contentDescription = "WIP"
                    )

                    Text(
                        modifier = Modifier.padding(top = 10.dp),
                        text = "Volume: ???",
                        color = textColor,
                        style = SemiTypo,
                        fontSize = 16.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

            }
            Row (
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(secondary),
                verticalAlignment = Alignment.CenterVertically
            ){
                Column (
                    modifier = Modifier
                        .padding(dpBoxMargin)
                        .padding(start = 20.dp)
                ) {
                    Text(
                        text = fitnessLvlAnnotatedString,
                        color = textColor,
                        style = SemiTypo,
                        fontSize = 16.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    Text(
                        modifier = Modifier.padding(top = 30.dp),
                        text = fitnessGoalAnnotatedString,
                        color = textColor,
                        style = SemiTypo,
                        fontSize = 16.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}

@Composable
private fun ProfileBanner(
    modifier: Modifier = Modifier,
    bgColor: Color,
    sendIntent: (ProfileIntent) -> Unit,
    state: ProfileState
) {
    val bannerPicWeight = 0.40f
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
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            val pfpWeight = 0.35f
            val usernameWeight = 0.55f
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
            val displayName = state.userData?.displayName ?: stringResource(R.string.display_name)
            val username = "@${state.userData?.username ?: "username"}"
            val date_joined = state.userData?.dateJoined?.let { "Joined $it" } ?: stringResource(R.string.date_joined)

            Column (
                modifier = Modifier
                    .weight(usernameWeight)
                    .align(Alignment.CenterVertically)
                    .padding(horizontal = 10.dp)
            ){
                Text(
                    text = displayName,
                    color = title,
                    style = TitleTypo,
                    fontSize = 20.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    modifier = Modifier.padding(top = 10.dp),
                    text = username,
                    color = onTertiary,
                    style = SemiTypo,
                    fontSize = 18.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    modifier = Modifier.padding(top = 10.dp),
                    text = date_joined,
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
                    onClick = {sendIntent(ProfileIntent.GoToSettings)},
                ) {
                    Icon(
                        modifier = Modifier.size(30.dp),
                        imageVector = Icons.Filled.Settings,
                        tint = onTertiary,
                        contentDescription = "Profile Settings"
                    )
                }
                IconButton(
                    onClick = {/*TODO*/ },
                ) {
                    Icon(
                        modifier = Modifier.size(30.dp),
                        imageVector = Icons.Filled.Edit,
                        tint = colorMessage,
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
                    modifier = Modifier.size(30.dp),
                    imageVector = Icons.Filled.MilitaryTech,
                    tint = colorMessage,
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
