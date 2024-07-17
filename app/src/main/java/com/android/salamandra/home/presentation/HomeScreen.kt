package com.android.salamandra.home.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.Construction
import androidx.compose.material.icons.outlined.GridView
import androidx.compose.material.icons.outlined.SwapVert
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.android.salamandra.R
import com.android.salamandra._core.domain.model.workout.WorkoutPreview
import com.android.salamandra._core.presentation.asUiText
import com.android.salamandra._core.presentation.components.ErrorDialog
import com.android.salamandra._core.presentation.components.FadeLip
import com.android.salamandra._core.presentation.components.ProfilePicture
import com.android.salamandra._core.presentation.components.WkTemplatePicture
import com.android.salamandra._core.presentation.components.bottomBar.MyBottomBarScaffold
import com.android.salamandra._core.util.WORKOUT_PREVIEW_LIST
import com.android.salamandra.destinations.EditWkScreenDestination
import com.android.salamandra.destinations.HomeScreenDestination
import com.android.salamandra.destinations.LoginScreenDestination
import com.android.salamandra.destinations.SeeWkScreenDestination
import com.android.salamandra.ui.theme.SalamandraTheme
import com.android.salamandra.ui.theme.TitleTypo
import com.android.salamandra.ui.theme.WkTemplateElementTypo
import com.android.salamandra.ui.theme.onTertiary
import com.android.salamandra.ui.theme.primaryVariant
import com.android.salamandra.ui.theme.tertiary
import com.android.salamandra.ui.theme.title
import com.android.salamandra.workouts.seeWk.presentation.SeeWkNavArgs
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator


@Destination
@Composable
fun HomeScreen(navigator: DestinationsNavigator, viewModel: HomeViewModel = hiltViewModel()) {
    val state by viewModel.state.collectAsState()
    val events by viewModel.events.collectAsState(initial = null)
    LaunchedEffect(events) {
        when (events) {
            HomeEvent.Logout -> navigator.navigate(LoginScreenDestination)
            HomeEvent.NavigateToEditWk -> navigator.navigate(EditWkScreenDestination())
            is HomeEvent.NavigateToSeeWk -> navigator.navigate(
                SeeWkScreenDestination(
                    SeeWkNavArgs(
                        wkTemplateId = (events as HomeEvent.NavigateToSeeWk).wkTemplateId
                    )
                )
            )

            is HomeEvent.BottomBarClicked -> navigator.navigate((events as HomeEvent.BottomBarClicked).destination)
            null -> {}
        }
    }

    ScreenBody(
        state = state,
        sendIntent = viewModel::dispatch,
    )
}

@Composable
private fun ScreenBody(
    state: HomeState,
    sendIntent: (HomeIntent) -> Unit,
) {
    MyBottomBarScaffold(
        currentDestination = HomeScreenDestination,
        onBottomBarClicked = { sendIntent(HomeIntent.BottomBarClicked(it)) }
    ) {
        val mainColor = tertiary;
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(mainColor),
            verticalArrangement = Arrangement.Top
        ) {
            HomeBanner(
                onCreateExercise = { sendIntent(HomeIntent.NewWk) },
                onSearchWkTemplate = {/* TODO */}
            )
            FadeLip()
            ListViewToggles()
            LazyColumn(modifier = Modifier.padding(start = 18.dp)) {
                items(state.wkPreviewList) { wkPreview ->
                    WkPreview(
                        wkPreview = wkPreview,
                        onClick = {sendIntent(HomeIntent.SeeWk(wkTemplateId = wkPreview.wkId))}
                    )
                    Spacer(modifier = Modifier.size(18.dp))
                }
            }

            if (state.error != null)
                ErrorDialog(
                    error = state.error.asUiText(),
                    onDismiss = { sendIntent(HomeIntent.CloseError) }
                )

        }

    }
}

@Composable
fun ListViewToggles() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
    ) {
        val iconColor = onTertiary
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = { }
            ) {
                Icon(
                    imageVector = Icons.Outlined.SwapVert,
                    tint = iconColor,
                    contentDescription = "Add workout"
                )
            }
            Text(
                text = "Name",
                style = WkTemplateElementTypo,
                color = iconColor,
                fontSize = 14.sp,
                modifier = Modifier.padding(start = 8.dp)
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(end = 10.dp),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = { }
            ) {
                Icon(
                    imageVector = Icons.Outlined.GridView,
                    tint = iconColor,
                    contentDescription = "Template display toggle"
                )
            }
        }
    }
}


@Composable
fun HomeBanner(
    onCreateExercise: () -> Unit,
    onSearchWkTemplate: () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(70.dp)
                .align(Alignment.TopStart),
            verticalAlignment = Alignment.Bottom
        )
        {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(modifier = Modifier.padding(start = 18.dp)) {
                    ProfilePicture(size = 42)
                }

                Text(
                    text = stringResource(R.string.your_workouts),
                    color = title,
                    fontSize = 22.sp,
                    style = TitleTypo,
                    modifier = Modifier.padding(start = 18.dp),
                    overflow = TextOverflow.Ellipsis
                )
            }

            Spacer(modifier = Modifier.weight(1f))
            Row(
                modifier = Modifier.padding(end = 10.dp)
            ) {
                IconButton(
                    onClick = { onSearchWkTemplate() }
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Search,
                        tint = title,
                        contentDescription = "Search workout"
                    )
                }
                IconButton(
                    onClick = { onCreateExercise() }
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Add,
                        tint = title,
                        contentDescription = "Add workout"
                    )
                }
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .align(Alignment.BottomEnd),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Outlined.Construction,
                contentDescription = "Construction icon",
                modifier = Modifier.size(20.dp),
                tint = primaryVariant,
            )
            Text(
                text = "WIP",
                color = primaryVariant,
                fontSize = 18.sp,
                modifier = Modifier.padding(start = 8.dp),
                style = TitleTypo,
            )
        }
    }
}


@Composable
fun WkPreview(
    wkPreview: WorkoutPreview,
    onClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .padding(end = 40.dp)
            .fillMaxWidth()
            .clickable { onClick() },
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        WkTemplatePicture(size = 50, shape = RoundedCornerShape(10))
        Spacer(modifier = Modifier.size(18.dp))
        Column {
            Text(
                text = wkPreview.name,
                color = title,
                fontSize = 15.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = "#tag1  #tag2",
                color = onTertiary,
                fontSize = 12.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}


@Preview
@Composable
fun LightPreview() {
    SalamandraTheme {
        ScreenBody(
            state = HomeState.initial.copy(wkPreviewList = WORKOUT_PREVIEW_LIST),
            sendIntent = {},
        )
    }
}