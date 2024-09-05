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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.GridView
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.SwapVert
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.android.salamandra.R
import com.android.salamandra._core.domain.model.workout.template.WorkoutPreview
import com.android.salamandra._core.presentation.asUiText
import com.android.salamandra._core.presentation.components.BottomSheet
import com.android.salamandra._core.presentation.components.ErrorDialog
import com.android.salamandra._core.presentation.components.FadeLip
import com.android.salamandra._core.presentation.components.IconShimmer
import com.android.salamandra._core.presentation.components.NotImplented
import com.android.salamandra._core.presentation.components.ProfilePicture
import com.android.salamandra._core.presentation.components.WkTemplatePicture
import com.android.salamandra._core.presentation.components.bottomBar.MyBottomBarScaffold
import com.android.salamandra._core.presentation.components.shimmerEffect
import com.android.salamandra._core.util.WORKOUT_PREVIEW_LIST
import com.android.salamandra.destinations.EditWkScreenDestination
import com.android.salamandra.destinations.HomeScreenDestination
import com.android.salamandra.destinations.LoginScreenDestination
import com.android.salamandra.destinations.SeeWkScreenDestination
import com.android.salamandra.ui.theme.SalamandraTheme
import com.android.salamandra.ui.theme.TitleTypo
import com.android.salamandra.ui.theme.WkTemplateElementTypo
import com.android.salamandra.ui.theme.onTertiary
import com.android.salamandra.ui.theme.tertiary
import com.android.salamandra.ui.theme.title
import com.android.salamandra.workouts.seeWk.presentation.SeeWkNavArgs
import com.android.salamandra.workouts.seeWk.presentation.components.TagRow
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

@OptIn(ExperimentalMaterial3Api::class)
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
                loading = state.loading,
                onCreateExercise = { sendIntent(HomeIntent.NewWk) },
                onSearchWkTemplate = { sendIntent(HomeIntent.ShowNotImplementedBanner) },
                onTag = { sendIntent(HomeIntent.ShowNotImplementedBanner) },
            )
            FadeLip()
            ListViewToggles(
                onToggleSort = { sendIntent(HomeIntent.ChangeSort) },
                onChangeAttribute = { sendIntent(HomeIntent.ShowNotImplementedBanner) },
                onViewToggle = { sendIntent(HomeIntent.ShowNotImplementedBanner) },
                loading = state.loading,
            )
            if (!state.loading) {
                val sortedList =
                    if (state.sortDescending) state.wkPreviewList.sortedByDescending { it.name }else state.wkPreviewList.sortedBy { it.name}
                LazyColumn(modifier = Modifier.padding(start = 18.dp)) {
                    items(sortedList) { wkPreview ->
                        WkPreview(
                            wkPreview = wkPreview,
                            onClick = { sendIntent(HomeIntent.SeeWk(wkTemplateId = wkPreview.wkId)) }
                        )
                        Spacer(modifier = Modifier.size(18.dp))
                    }
                }
            } else {
                Column(modifier = Modifier.padding(start = 18.dp)) {
                    for (i in 0..5) {
                        LoadingWkPreview()
                        Spacer(Modifier.size(18.dp))
                    }
                }
            }

            if(state.loading)
                Box(Modifier.fillMaxSize().background(tertiary.copy(alpha = 0.95f)))

            if (state.error != null)
                ErrorDialog(
                    error = state.error.asUiText(),
                    onDismiss = { sendIntent(HomeIntent.CloseError) }
                )

        }

        if (state.notImplementedBanner) {
            val sheetState = rememberModalBottomSheetState(
                skipPartiallyExpanded = false,
            )
            BottomSheet(
                sheetState = sheetState,
                onDismiss = { sendIntent(HomeIntent.HideBottomSheet) },
                content = { NotImplented() }
            )
        }
    }
}

@Composable
fun ListViewToggles(
    onToggleSort: () -> Unit,
    onViewToggle: () -> Unit,
    onChangeAttribute: () -> Unit,
    loading: Boolean,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
    ) {
        val iconColor = onTertiary
        Row(
            Modifier.padding(start = 5.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (loading) {
                IconShimmer(Modifier.padding(start = 12.dp))
                Modifier
                    .padding(start = 12.dp)
                    .height(16.dp)
                    .width(40.dp)
                    .shimmerEffect()
            }
            else {
                IconButton(
                    onClick = { onToggleSort() }
                ) {
                    Icon(
                        imageVector = Icons.Outlined.SwapVert,
                        tint = iconColor,
                        contentDescription = "Add workout"
                    )
                }
                Text(
                    modifier = Modifier
                        .clickable { onChangeAttribute() },
                    text = stringResource(R.string.name),
                    style = WkTemplateElementTypo,
                    color = iconColor,
                    fontSize = 14.sp,
                )
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(end = 10.dp),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (loading) IconShimmer()
            else {
                IconButton(
                    onClick = { onViewToggle() }
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
}


@Composable
fun HomeBanner(
    loading: Boolean,
    loadingBoxColor: Color = Color.Gray,
    onCreateExercise: () -> Unit,
    onSearchWkTemplate: () -> Unit,
    onTag: () -> Unit
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
                if (loading)
                    Box(
                        Modifier
                            .padding(start = 18.dp)
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(loadingBoxColor)
                            .shimmerEffect()
                    )
                else {
                    Box(modifier = Modifier.padding(start = 18.dp)) {
                        ProfilePicture(size = 42)
                    }
                }

                if (loading)
                    Box(
                        Modifier
                            .padding(horizontal = 16.dp)
                            .width(160.dp)
                            .height(24.dp)
                            .background(loadingBoxColor)
                            .shimmerEffect()
                    )
                else {
                    Text(
                        text = stringResource(R.string.your_workouts),
                        color = title,
                        fontSize = 22.sp,
                        style = TitleTypo,
                        modifier = Modifier.padding(start = 18.dp),
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))
            Row(
                modifier = Modifier.padding(end = 10.dp)
            ) {
                if (loading) {
                    IconShimmer()
                    Spacer(Modifier.size(20.dp))
                    IconShimmer()

                } else {
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
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .align(Alignment.BottomEnd),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (loading)
                Box(
                    Modifier
                        .width(52.dp)
                        .height(20.dp)
                        .background(loadingBoxColor)
                        .shimmerEffect()
                )
            else {
                TagRow(
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 12.dp),
                    onClick = onTag
                )
            }
        }
    }
}


@Composable
fun WkPreview(
    loadingBoxColor: Color = Color.Gray,
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
        WkTemplatePicture(
            size = 50,
            loadingColor = loadingBoxColor,
            shape = RoundedCornerShape(10)
        )
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

@Composable
fun LoadingWkPreview(
    loadingBoxColor: Color = Color.Gray,
) {
    Row(
        modifier = Modifier
            .padding(end = 40.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        WkTemplatePicture(
            size = 50,
            loading = true,
            loadingColor = loadingBoxColor,
            shape = RoundedCornerShape(10)
        )
        Spacer(modifier = Modifier.size(18.dp))
        Column {
            Box(
                Modifier
                    .width(180.dp)
                    .height(18.dp)
                    .background(loadingBoxColor)
                    .shimmerEffect()
            )
            Spacer(Modifier.size(8.dp))
            Box(
                Modifier
                    .width(80.dp)
                    .height(12.dp)
                    .background(loadingBoxColor)
                    .shimmerEffect()
            )
        }
    }
}


@Preview
@Composable
fun Preview() {
    SalamandraTheme {
        ScreenBody(
            state = HomeState.initial.copy(wkPreviewList = WORKOUT_PREVIEW_LIST, loading = false),
            sendIntent = {},
        )
    }
}
