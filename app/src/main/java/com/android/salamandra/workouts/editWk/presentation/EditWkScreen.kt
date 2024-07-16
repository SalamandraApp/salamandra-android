package com.android.salamandra.workouts.editWk.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.FitnessCenter
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import com.android.salamandra.R
import com.android.salamandra._core.presentation.components.BottomSheet
import com.android.salamandra._core.presentation.components.ExerciseInfo
import com.android.salamandra._core.presentation.components.FadeLip
import com.android.salamandra._core.presentation.components.TabRowBuilder
import com.android.salamandra._core.util.WORKOUT_TEMPLATE
import com.android.salamandra.destinations.SearchExerciseScreenDestination
import com.android.salamandra.ui.theme.TitleTypo
import com.android.salamandra.ui.theme.onTertiary
import com.android.salamandra.ui.theme.secondary
import com.android.salamandra.ui.theme.tertiary
import com.android.salamandra.ui.theme.title
import com.android.salamandra.workouts.commons.presentation.components.WkElementComponent
import com.android.salamandra.workouts.commons.presentation.components.WkTemplateViewLabels
import com.android.salamandra.workouts.commons.presentation.constants.WkTemplateScreenConstants
import com.android.salamandra.workouts.editWk.presentation.components.BannerTitleRow
import com.android.salamandra.workouts.editWk.presentation.components.ButtonsRowBanner
import com.android.salamandra.workouts.editWk.presentation.components.EditExercise
import com.android.salamandra.workouts.editWk.presentation.components.EditTagRow
import com.android.salamandra.workouts.editWk.presentation.components.EditWkBannerTopRow
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Destination(navArgsDelegate = EditWkNavArgs::class)
@Composable
fun EditWkScreen(navigator: DestinationsNavigator, viewModel: EditWkViewModel = hiltViewModel()) {
    val state by viewModel.state.collectAsState()
    val events by viewModel.events.collectAsState(initial = null)
    LaunchedEffect(events) {
        when (events) {
            EditWkEvent.NavigateUp -> navigator.navigateUp()
            EditWkEvent.NavigateToEdit -> {}
            EditWkEvent.NavigateToSearch -> navigator.navigate(SearchExerciseScreenDestination)
            null -> {}
        }
    }
    ScreenBody(
        state = state,
        sendIntent = viewModel::dispatch
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ScreenBody(
    state: EditWkState,
    sendIntent: (EditWkIntent) -> Unit,
) {
    val mainColor = tertiary
    val bannerHeight = WkTemplateScreenConstants.bannerHeight
    val fixedBannerHeight = WkTemplateScreenConstants.fixedBannerHeight

    val scrollThreshold: Float
    val bannerHeightPx: Float
    with(LocalDensity.current) {
        scrollThreshold = (bannerHeight - fixedBannerHeight).toPx()
        bannerHeightPx = bannerHeight.toPx()
    }

    val listState = rememberLazyListState()
    val scrolledPast = remember {
        derivedStateOf {
            val firstVisibleItem = listState.firstVisibleItemIndex
            val firstVisibleItemScrollOffset = listState.firstVisibleItemScrollOffset
            val totalScrollOffset = firstVisibleItem * bannerHeightPx + firstVisibleItemScrollOffset
            totalScrollOffset > scrollThreshold
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(mainColor)
    ) {

        if (scrolledPast.value) {
            Column (
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.TopCenter)
                    .zIndex(1f),
            ){
                EditWkFixedBanner(
                    state = state,
                    sendIntent = sendIntent,
                    modifier = Modifier
                        .height(fixedBannerHeight)
                        .background(mainColor)
                )
                FadeLip()
            }
        }
        LazyColumn(
            state = listState,
            modifier = Modifier
                .fillMaxSize()
                .background(mainColor)
                .align(Alignment.TopStart),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            val startPad = 15.dp
            item {
                EditWkBigBanner(
                    modifier = Modifier.height(bannerHeight),
                    state = state,
                    sendIntent = sendIntent,
                    backgroundColor = mainColor
                )
                FadeLip()
                Spacer(modifier = Modifier.size(5.dp))
            }
            itemsIndexed(state.wkTemplate.elements) { index, element ->
                WkElementComponent(
                    onOption = {sendIntent(EditWkIntent.ShowBottomSheet(index))},
                    wkElement = element,
                    startPad = startPad,
                    fgColor = secondary,
                )
            }
        }
        if (state.bottomSheet && state.exerciseSelectedIndex != null) {
            val sheetState = rememberModalBottomSheetState(
                skipPartiallyExpanded = false,
            )
            BottomSheet(
                sheetState = sheetState,
                onDismiss = { sendIntent(EditWkIntent.HideBottomSheet) },
                content = {
                    TabRowBuilder(
                        contents = listOf(
                            { EditExercise(index = 0, sendIntent = sendIntent) },
                            { ExerciseInfo() }
                        ),
                        icons = listOf(Icons.Outlined.Edit, Icons.Outlined.FitnessCenter),
                        titles = listOf("Edit", "Info")
                    )
                }
            )
        }
    }
}

@Composable
fun EditWkFixedBanner(
    state: EditWkState,
    sendIntent: (EditWkIntent) -> Unit,
    modifier: Modifier = Modifier
) {

    val dpSideMargin = WkTemplateScreenConstants.sideMargin
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = dpSideMargin),
        verticalArrangement = Arrangement.Bottom
    ) {
        EditWkBannerTopRow(
            modifier = Modifier.weight(1f),
            sendIntent = sendIntent,
            state = state,
            middleContent = {
                Text(
                    text = state.wkTemplate.name,
                    color = title,
                    fontSize = 16.sp,
                    style = TitleTypo,
                )
            }
        )
        WkTemplateViewLabels()
    }
}

@Composable
fun EditWkBigBanner(
    modifier: Modifier = Modifier,
    state: EditWkState,
    sendIntent: (EditWkIntent) -> Unit,
    backgroundColor: Color
) {

    val dpTopRow     = WkTemplateScreenConstants.bannerRowHeights.top
    val dpTitle      = WkTemplateScreenConstants.bannerRowHeights.picture
    val dpTags       = WkTemplateScreenConstants.bannerRowHeights.tags
    val dpButtons    = WkTemplateScreenConstants.bannerRowHeights.buttons
    val dpLabels     = WkTemplateScreenConstants.bannerRowHeights.labels

    val dpSideMargin = WkTemplateScreenConstants.sideMargin
    val dpInBetweenMargin = WkTemplateScreenConstants.bannerInBetweenMargin
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(backgroundColor)
            .padding(horizontal = dpSideMargin)
    ) {
        EditWkBannerTopRow(
            modifier = Modifier
                .height(dpTopRow),
            sendIntent = sendIntent,
            state = state,
            middleContent = {
                Text(
                    text = stringResource(R.string.edit_workout),
                    color = onTertiary,
                    fontSize = 16.sp,
                    style = TitleTypo,
                )
            }
        )
        BannerTitleRow(
            modifier = Modifier
                .height(dpTitle)
                .padding(bottom = dpInBetweenMargin),
            sendIntent = sendIntent,
            state = state
        )
        EditTagRow(
            modifier = Modifier
                .padding(bottom = dpInBetweenMargin)
                .height(dpTags),
            state = state,
            sendIntent = sendIntent
        )
        ButtonsRowBanner(
            modifier = Modifier
                .height(dpButtons)
                .padding(bottom = dpInBetweenMargin / 2),
            state = state,
            sendIntent = sendIntent
        )
        WkTemplateViewLabels(
            modifier = Modifier
                .height(dpLabels),
        )
    }
}


@Preview
@Composable
private fun EditWkPreview() {
    ScreenBody(
        state = EditWkState.initial.copy(
            wkTemplate = WORKOUT_TEMPLATE,
        ),
        sendIntent = {},
    )
}