package com.android.salamandra.profile.presentation

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Construction
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.MilitaryTech
import androidx.compose.material.icons.filled.QueryStats
import androidx.compose.material.icons.filled.Scale
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Scale
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.android.salamandra.R
import com.android.salamandra._core.domain.USER_WEIGHT_MAX
import com.android.salamandra._core.domain.USER_WEIGHT_MIN
import com.android.salamandra._core.domain.model.enums.FitnessGoal
import com.android.salamandra._core.domain.model.enums.FitnessLevel
import com.android.salamandra._core.presentation.components.EditWeight
import com.android.salamandra._core.presentation.components.FadeLip
import com.android.salamandra._core.presentation.components.GradientSlider
import com.android.salamandra._core.presentation.components.IconShimmer
import com.android.salamandra._core.presentation.components.ProfilePicture
import com.android.salamandra._core.presentation.components.WkTemplatePicture
import com.android.salamandra._core.presentation.components.bottomBar.MyBottomBarScaffold
import com.android.salamandra._core.presentation.components.shimmerEffect
import com.android.salamandra.destinations.LoginScreenDestination
import com.android.salamandra.destinations.ProfileScreenDestination
import com.android.salamandra.destinations.SettingsScreenDestination
import com.android.salamandra.ui.theme.SalamandraTheme
import com.android.salamandra.ui.theme.SemiTypo
import com.android.salamandra.ui.theme.TitleTypo
import com.android.salamandra.ui.theme.colorError
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
import java.time.LocalDate
import java.time.format.DateTimeFormatter

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
                modifier = Modifier.weight(bannerWeight),
                loading = state.loading,
                username = state.userData?.displayName,
                displayName = state.userData?.displayName,
                dateJoined = state.userData?.dateJoined,
                onGoToSettings = { sendIntent(ProfileIntent.GoToSettings) }
            )
            FadeLip()
            InfoSection(
                modifier = Modifier
                    .weight(infoWeight),
                loading = state.loading,

                weight = state.userData?.weight,
                editWeight = state.editWeight,
                newWeight = state.newWeight,
                onWeight = { sendIntent(ProfileIntent.OpenEditWeight) },
                onEditWeight = { sendIntent(ProfileIntent.EditWeight(it))},
                onSaveWeight = { sendIntent(ProfileIntent.SaveNewWeight) },

                fitnessLevel = state.userData?.fitnessLevel,
                fitnessGoal = state.userData?.fitnessGoal,
            )
        }
        if(state.loading)
            Box(Modifier.fillMaxSize().background(tertiary.copy(alpha = 0.9f)))

        if (!state.isSignedIn && !state.loading) {
            NotLoggedInCover(
                onGoToLogin = { sendIntent(ProfileIntent.GoToLogin) }
            )
        }
    }

}

@Composable
private fun NotLoggedInCover(
    onGoToLogin: () -> Unit
) {
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

        Spacer(modifier = Modifier.weight(spacerWeights / 2))
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
            onClick = { onGoToLogin() }
        ) {
            Text(
                text = stringResource(R.string.login),
                color = tertiary,
                fontSize = 20.sp
            )
        }
        Spacer(modifier = Modifier.weight(spacerWeights / 2))
    }
}

@Composable
private fun InfoSection(
    modifier: Modifier = Modifier,
    loading: Boolean,
    weight: Double?,
    newWeight: Double?,
    editWeight: Boolean,
    onWeight: () -> Unit,
    onEditWeight: (Double) -> Unit,
    onSaveWeight: () -> Unit,
    fitnessLevel: FitnessLevel?,
    fitnessGoal: FitnessGoal?,
) {
    val textColor = subtitle
    val iconColor = onSecondary
    val wipWidget = @Composable {
        Column(
            Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                modifier = Modifier.size(60.dp),
                imageVector = Icons.Default.Construction,
                tint = colorMessage,
                contentDescription = null
            )
            Spacer(Modifier.weight(1f))
            Text(
                modifier = Modifier.padding(top = 10.dp),
                text = "Work in Progress",
                color = colorMessage,
                style = SemiTypo,
                fontSize = 16.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
    val weightWidget = @Composable {
        val weightAnnotatedString = buildAnnotatedString {
            append("Weight: ")
            withStyle(style = SpanStyle(color = textColor.copy(alpha = 0.5f))) {
                append(weight?.toString() ?: "???")
                append(" kg")
            }
        }
        if (editWeight && newWeight != null) {
            Column(
                Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                val focusRequester = remember { FocusRequester() }
                val keyboardController = LocalSoftwareKeyboardController.current
                val weightKg = buildAnnotatedString {
                    append(stringResource(R.string.weight))
                    withStyle(style = SpanStyle(color = textColor.copy(alpha = 0.5f))) {
                        append(" kg")
                    }
                }
                Text(
                    modifier = Modifier.padding(top = 10.dp),
                    text = weightKg,
                    color = textColor,
                    style = SemiTypo,
                    fontSize = 16.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(Modifier.weight(1f))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(Modifier.weight(1f)) {
                        EditWeight(
                            modifier = Modifier.focusRequester(focusRequester),
                            weight = newWeight,
                            onEditWeight = onEditWeight
                        )
                    }
                    LaunchedEffect(Unit) {
                        focusRequester.requestFocus()
                        keyboardController?.show()
                    }
                    FloatingActionButton(
                        modifier = Modifier.weight(0.8f).padding(start = 10.dp),
                        containerColor = primaryVariant.copy(0.3f),
                        contentColor = primaryVariant,
                        elevation = FloatingActionButtonDefaults.elevation(0.dp),
                        onClick = { onSaveWeight() }) {
                        Icon(
                            imageVector = Icons.Filled.CheckCircle,
                            contentDescription = "Add Exercise",
                        )

                    }
                }
            }
        } else {
            Column(
                Modifier.fillMaxSize().clickable { onWeight() },
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    modifier = Modifier.size(60.dp),
                    imageVector = Icons.Outlined.Scale,
                    tint = iconColor,
                    contentDescription = "WIP"
                )
                Spacer(Modifier.weight(1f))
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
        }
    }

    LazyColumn (
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp)
            .padding(top = 20.dp)
    ) {
        item {
            ProfileInfoRow(
                loading = loading,
                Modifier.height(150.dp).padding(bottom = 10.dp),
                contents = listOf(weightWidget, wipWidget)
            )
        }
        item {
            ProfileInfoRow(
                loading = loading,
                Modifier.height(200.dp).padding(bottom = 10.dp),
                contents = listOf({Box(Modifier.fillMaxSize().clickable { Log.i("Clicked Widget", "Widget 1") }){}})
            )
        }
    }
}



@Composable
private fun ProfileInfoRow(
    loading: Boolean,
    modifier: Modifier = Modifier,
    contents: List<@Composable () -> Unit>,
) {
    if (contents.size > 3 || contents.isEmpty())
        throw IllegalArgumentException("More than 3 components is too much")

    Row (
        modifier = modifier
            .fillMaxWidth()
    ) {
        contents.forEachIndexed { index, element ->
            val endPad = if (index == contents.size - 1) 0.dp else 10.dp
            Box(
                Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .padding(end = endPad)
                    .clip(RoundedCornerShape(10.dp))
                    .background(secondary)
                    .padding(20.dp)
                    .then(if (loading) Modifier.shimmerEffect() else Modifier),
                contentAlignment = Alignment.Center
            ) {
                if (!loading) element()
            }
        }
    }
}


@Composable
private fun ProfileBanner(
    modifier: Modifier = Modifier,
    loading: Boolean,
    username: String?,
    displayName: String?,
    dateJoined: LocalDate?,
    onGoToSettings: () -> Unit,
) {
    val bannerPicWeight = 0.40f
    val bannerPfpWeight = 0.5f
    val bannerBadgesWeight = 0.15f

    Column(
        modifier = modifier
            .fillMaxWidth()
    ) {
        if (loading)
            Box(
                Modifier
                    .fillMaxWidth()
                    .weight(bannerPfpWeight)
                    .background(Color.Gray)
                    .shimmerEffect()
            )
        else {
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
        }

        val sideMargin = 25.dp
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .weight(bannerPfpWeight),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            val pfpWeight = 0.35f
            val usernameWeight = 0.55f
            val buttonsWeight = 0.1f
            if (loading)
                Box(
                    Modifier
                        .fillMaxHeight()
                        .weight(pfpWeight)
                        .padding(start = 32.dp)
                        .padding(vertical = 22.dp)
                        .clip(CircleShape)
                        .background(Color.Gray)
                        .shimmerEffect()
                )
            else {
                Column(
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
            }
            val displayNameText = displayName ?: stringResource(R.string.display_name)
            val usernameText = "@${username ?: "username"}"
            val dateJoinedText =
                "Joined ${dateJoined?.format(DateTimeFormatter.ofPattern("dd-MM-yyyy")) ?: "DD-MM-YYYY"}"

            Column(
                modifier = Modifier
                    .weight(usernameWeight)
                    .align(Alignment.CenterVertically)
                    .padding(horizontal = 10.dp)
            ) {
                if (loading) {
                    Spacer(Modifier.weight(1f))
                    Box(
                        Modifier
                            .fillMaxWidth()
                            .height(24.dp)
                            .padding(end = 32.dp)
                            .background(Color.Gray)
                            .shimmerEffect()
                    )
//                    Spacer(Modifier.size(12.dp))
                    Spacer(Modifier.weight(0.5f))
                    Box(
                        Modifier
                            .fillMaxWidth()
                            .height(18.dp)
                            .padding(end = 70.dp)
                            .background(Color.Gray)
                            .shimmerEffect()
                    )
                    Spacer(Modifier.weight(0.5f))
                    Box(
                        Modifier
                            .fillMaxWidth()
                            .height(16.dp)
                            .padding(end = 38.dp)
                            .background(Color.Gray)
                            .shimmerEffect()
                    )
                    Spacer(Modifier.weight(1f))
                } else {
                    Text(
                        text = displayNameText,
                        color = title,
                        style = TitleTypo,
                        fontSize = 20.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        modifier = Modifier.padding(top = 10.dp),
                        text = usernameText,
                        color = onTertiary,
                        style = SemiTypo,
                        fontSize = 18.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        modifier = Modifier.padding(top = 10.dp),
                        text = dateJoinedText,
                        color = onTertiary,
                        style = SemiTypo,
                        fontSize = 14.sp
                    )
                }
            }
            Column(
                modifier = Modifier
                    .weight(buttonsWeight)
                    .align(Alignment.CenterVertically)
                    .padding(end = sideMargin - 10.dp)
            ) {
                if (loading) {
                    Spacer(Modifier.weight(1f))
                    IconShimmer()
                    Spacer(Modifier.weight(0.8f))
                    IconShimmer()
                    Spacer(Modifier.weight(1f))
                } else {
                    IconButton(
                        onClick = { onGoToSettings() },
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
                            imageVector = Icons.Outlined.Edit,
                            tint = onTertiary,
                            contentDescription = "WIP"
                        )
                    }
                }
            }
        }
    }
}


@Preview()
@Composable
private fun ScreenPreview() {
    SalamandraTheme {
        ScreenBody(
            state = ProfileState.initial.copy(isSignedIn = true, loading = false),
            sendIntent = {}
        )
    }
}

//@Preview()
//@Composable
//private fun LoadingScreenPreview() {
//    SalamandraTheme {
//        ScreenBody(
//            state = ProfileState.initial.copy(isSignedIn = true, loading = true),
//            sendIntent = {}
//        )
//    }
//}
