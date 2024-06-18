package com.android.salamandra.home.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Search
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
import com.android.salamandra._core.domain.model.workout.WorkoutPreview
import com.android.salamandra._core.presentation.asUiText
import com.android.salamandra._core.presentation.components.ErrorDialog
import com.android.salamandra._core.presentation.components.MyColumn
import com.android.salamandra._core.presentation.components.MyRow
import com.android.salamandra._core.presentation.components.MySpacer
import com.android.salamandra._core.presentation.components.ProfilePicture
import com.android.salamandra._core.presentation.components.WkPlaceholder
import com.android.salamandra._core.presentation.components.bottomBar.MyBottomBarScaffold
import com.android.salamandra._core.util.WORKOUT_PREVIEW_LIST
import com.android.salamandra.destinations.EditWkScreenDestination
import com.android.salamandra.destinations.HomeScreenDestination
import com.android.salamandra.destinations.LoginScreenDestination
import com.android.salamandra.ui.theme.SalamandraTheme
import com.android.salamandra.ui.theme.TitleTypo
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
                .background(tertiary)
                .padding(12.dp),
            verticalArrangement = Arrangement.Top
        ) {
            MyRow {
                ProfilePicture(size = 50)
                MySpacer(size = 12)
                Text(
                    text = stringResource(R.string.your_workouts),
                    color = title,
                    fontSize = 26.sp,
                    style = TitleTypo
                )
                Spacer(modifier = Modifier.weight(1f))
                MyRow {
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
            MySpacer(size = 12)
            LazyColumn() {
                items(state.wkPreviewList) { wkPreview ->
                    MyWkPreview(wkPreview = wkPreview)
                    MySpacer(size = 8)
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
fun MyWkPreview(wkPreview: WorkoutPreview) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        WkPlaceholder(size = 60, shape = RoundedCornerShape(10))
        MySpacer(size = 12)
        Text(text = wkPreview.name, color = title, fontSize = 18.sp)
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