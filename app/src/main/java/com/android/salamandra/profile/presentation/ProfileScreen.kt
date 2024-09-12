package com.android.salamandra.profile.presentation

import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.windowInsetsEndWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Construction
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Pending
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.android.salamandra.R
import com.android.salamandra._core.domain.model.enums.FitnessGoal
import com.android.salamandra._core.domain.model.enums.FitnessLevel
import com.android.salamandra._core.domain.model.enums.getIcon
import com.android.salamandra._core.domain.model.enums.toFitnessLevel
import com.android.salamandra._core.domain.model.enums.toInt
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
import com.android.salamandra.profile.presentation.components.fitnessWdiget
import com.android.salamandra.profile.presentation.components.weightWidget
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

                editFitness = state.editFitness,
                onFitness = { sendIntent(ProfileIntent.OpenEditFitness(it)) },
                onEditFitness = { it1, it2, it3 -> sendIntent(ProfileIntent.EditFitness(it1, it2, it3)) },
                onSaveFitness = { sendIntent(ProfileIntent.SaveFitness) },
                fitnessLevel = state.userData?.fitnessLevel,
                fitnessGoal = state.userData?.fitnessGoal,
                newLevel = state.newFitnessLevel,
                newGoal = state.newFitnessGoal
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
    
    editFitness: String,
    onSaveFitness: () -> Unit,
    onFitness: (String) -> Unit,
    onEditFitness: (String, FitnessLevel?, FitnessGoal?) -> Unit,
    newLevel: FitnessLevel?,
    newGoal: FitnessGoal?,
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
                contents = listOf(
                    {weightWidget(
                        textColor = textColor,
                        iconColor = iconColor,
                        onWeight = onWeight,
                        onEditWeight = onEditWeight,
                        onSaveWeight = onSaveWeight,
                        editWeight = editWeight,
                        newWeight = newWeight,
                        weight = weight
                    )},
                    wipWidget
                )
            )
        }
        item {
            ProfileInfoRow(
                loading = loading,
                Modifier.height(if (editFitness == "") 150.dp else 180.dp).padding(bottom = 10.dp),
                contents = listOf {
                    fitnessWdiget(
                        textColor = textColor,
                        iconColor = iconColor,
                        editFitness = editFitness,
                        onSaveFitness = onSaveFitness,
                        onFitness = onFitness,
                        onEditFitness = onEditFitness,
                        newLevel = newLevel,
                        newGoal = newGoal,
                        fitnessLevel = fitnessLevel,
                        fitnessGoal = fitnessGoal
                    )
                }
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
                    .then(if (loading) Modifier.shimmerEffect() else Modifier)
                    .padding(20.dp),
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
    val sideMargin = 25.dp

    Column(
        modifier = modifier
            .fillMaxSize()
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .weight(bannerPicWeight)
                .then(if (loading) Modifier.shimmerEffect() else Modifier)
        ) {
            if (!loading)
            WkTemplatePicture(modifier = Modifier.fillMaxSize())
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = sideMargin)
                .weight(bannerPfpWeight),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            Column(
                modifier = Modifier
                    .width(120.dp)
                    .then(if (loading)
                        Modifier.aspectRatio(1f).clip(RoundedCornerShape(50)).shimmerEffect()
                    else Modifier)
            ) {
                if (!loading)
                    ProfilePicture(
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(1f),
                    )
            }

            val displayNameText = displayName ?: stringResource(R.string.display_name)
            val usernameText = "@${username ?: "username"}"
            val dateJoinedText =
                "Joined ${dateJoined?.format(DateTimeFormatter.ofPattern("dd-MM-yyyy")) ?: "DD-MM-YYYY"}"

            Column(
                modifier = Modifier
                    .weight(1f)
                    .align(Alignment.CenterVertically)
                    .padding(start = 15.dp)
            ) {
                Row (
                    Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ){
                    Text(
                        modifier = Modifier
                            .then(
                                if (loading) Modifier.clip(RoundedCornerShape(40))
                                    .shimmerEffect() else Modifier
                            ),
                        text = displayNameText,
                        color = if (!loading) title else Color.Transparent,
                        style = TitleTypo,
                        fontSize = 20.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(Modifier.weight(1f))
                    IconButton(
                        modifier = Modifier.size(35.dp),
                        onClick = { onGoToSettings() },
                    ) {
                        Icon(
                            modifier = modifier.then(if (loading) Modifier.shimmerEffect() else Modifier),
                            imageVector = Icons.Filled.Settings,
                            tint = if (loading) Color.Transparent else onTertiary,
                            contentDescription = "Profile Settings"
                        )
                    }
                }
                Text(
                    modifier = Modifier
                        .padding(top = 5.dp)
                        .then(if (loading) Modifier.clip(RoundedCornerShape(40)).shimmerEffect() else Modifier),
                    text = usernameText,
                    color = if (!loading) onTertiary else Color.Transparent,
                    style = SemiTypo,
                    fontSize = 18.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Text(
                    modifier = Modifier
                        .padding(top = 10.dp)
                        .then(if (loading) Modifier.clip(RoundedCornerShape(40)).shimmerEffect() else Modifier),
                    text = dateJoinedText,
                    color = if (!loading) onTertiary else Color.Transparent,
                    style = SemiTypo,
                    fontSize = 14.sp
                )
            }
        }
    }
}


@Preview()
@Composable
private fun ScreenPreview1() {
    SalamandraTheme {
        ScreenBody(
            state = ProfileState.initial.copy(isSignedIn = true, loading = false, editFitness = "goal"),
            sendIntent = {}
        )
    }
}