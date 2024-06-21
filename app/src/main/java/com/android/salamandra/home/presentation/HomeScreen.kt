package com.android.salamandra.home.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.android.salamandra.R
import com.android.salamandra._core.domain.model.workout.WorkoutPreview
import com.android.salamandra._core.presentation.asUiText
import com.android.salamandra._core.presentation.components.ErrorDialog
import com.android.salamandra._core.presentation.components.MyColumn
import com.android.salamandra._core.presentation.components.MySpacer
import com.android.salamandra._core.presentation.components.ProfilePicture
import com.android.salamandra._core.presentation.components.WkPlaceholder
import com.android.salamandra._core.presentation.components.bottomBar.MyBottomBarScaffold
import com.android.salamandra._core.util.WORKOUT_PREVIEW_LIST
import com.android.salamandra.destinations.EditWkScreenDestination
import com.android.salamandra.destinations.HomeScreenDestination
import com.android.salamandra.destinations.LoginScreenDestination
import com.android.salamandra.ui.theme.Normal
import com.android.salamandra.ui.theme.SalamandraTheme
import com.android.salamandra.ui.theme.TitleTypo
import com.android.salamandra.ui.theme.WkTemplateElementTypo
import com.android.salamandra.ui.theme.colorError
import com.android.salamandra.ui.theme.onSecondary
import com.android.salamandra.ui.theme.primary
import com.android.salamandra.ui.theme.primaryVariant
import com.android.salamandra.ui.theme.secondary
import com.android.salamandra.ui.theme.tertiary
import com.android.salamandra.ui.theme.title
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
        MyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(tertiary),
            verticalArrangement = Arrangement.Top
        ) {
            MyHomeBanner(sendIntent)
            MyFadeLip()
            MyViewToggles()
            LazyColumn(modifier = Modifier.padding(start = 18.dp)) {
                items(state.wkPreviewList) { wkPreview ->
                    MyWkPreview(wkPreview = wkPreview)
                    MySpacer(size = 18)
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
fun MyViewToggles() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = { }
            ) {
                Icon(
                    imageVector = Icons.Outlined.SwapVert,
                    tint = onSecondary,
                    contentDescription = "Add workout"
                )
            }
            Text(
                text = "WIP",
                style = WkTemplateElementTypo,
                color = onSecondary,
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
                    tint = onSecondary,
                    contentDescription = "Info"
                )
            }
        }
    }
}


@Composable
fun MyHomeBanner(sendIntent: (HomeIntent) -> Unit) {
    Box (
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp)
            .background(tertiary)

    ) {
        Row (
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
                    modifier = Modifier.padding(start = 18.dp)
                )
            }

            Spacer(modifier = Modifier.weight(1f))
            Row(
                modifier = Modifier.padding(end = 10.dp)
            ) {
                IconButton(
                    onClick = {/*TODO*/ }
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Search,
                        tint = title,
                        contentDescription = "Search workout"
                    )
                }
                IconButton(
                    onClick = { sendIntent(HomeIntent.NewWk) }
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
fun MyFadeLip() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(6.dp)
            .background(tertiary)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(6.dp)
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color.Black.copy(alpha = 0.6f),
                            Color.Black.copy(alpha = 0.0f)
                        )
                    )
                )
        ) {}
    }
}

@Composable
fun MyWkPreview(wkPreview: WorkoutPreview) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        WkPlaceholder(size = 50, shape = RoundedCornerShape(10))
        MySpacer(size = 18)
        Column {
            Text(text = wkPreview.name, color = title, fontSize = 15.sp)
            Text(text = "WIP", color = onSecondary, fontSize = 12.sp)
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