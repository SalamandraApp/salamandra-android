package com.android.salamandra.workouts.seeWk.presentation

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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import com.android.salamandra._core.presentation.components.BottomSheet
import com.android.salamandra._core.presentation.components.ExerciseInfo
import com.android.salamandra._core.presentation.components.FadeLip
import com.android.salamandra.workouts.commons.presentation.components.WkElementComponent
import com.android.salamandra.workouts.commons.presentation.constants.WkTemplateScreenConstants
import com.android.salamandra.workouts.commons.presentation.components.WkTemplateViewLabels
import com.android.salamandra._core.util.WORKOUT_TEMPLATE
import com.android.salamandra.ui.theme.TitleTypo
import com.android.salamandra.ui.theme.tertiary
import com.android.salamandra.ui.theme.title
import com.android.salamandra.workouts.seeWk.presentation.components.BannerTitleRow
import com.android.salamandra.workouts.seeWk.presentation.components.ButtonsRow
import com.android.salamandra.workouts.seeWk.presentation.components.SeeWkBannerTopRow
import com.android.salamandra.workouts.seeWk.presentation.components.TagRow
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator


@Destination(navArgsDelegate = SeeWkNavArgs::class)
@Composable
fun SeeWkScreen(navigator: DestinationsNavigator, viewModel: SeeWkViewModel = hiltViewModel()) {
    val state by viewModel.state.collectAsState()
    val events by viewModel.events.collectAsState(initial = null)
    LaunchedEffect(events) {
        when (events) {
            SeeWkEvent.NavigateUp -> navigator.navigateUp()
            // TODO: navigate to EditWk
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
    state: SeeWkState,
    sendIntent: (SeeWkIntent) -> Unit
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
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.TopCenter)
                    .zIndex(1f),
            ) {
                SeeWkFixedBanner(
                    wkName = state.wkTemplate.name,
                    onGoBack = {sendIntent(SeeWkIntent.NavigateUp)},
                    onExecuteWk = {/* TODO */},
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
                .background(mainColor),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            item {
                SeeWkBigBanner(
                    modifier = Modifier.height(bannerHeight),
                    wkName = state.wkTemplate.name,
                    wkDescription = state.wkTemplate.description,
                    onGoBack = { sendIntent(SeeWkIntent.NavigateUp) },
                    onExecuteWk = { }
                )
                FadeLip()
                Spacer(modifier = Modifier.size(5.dp))
            }
            items(state.wkTemplate.elements) {
                WkElementComponent(
                    onOption = {sendIntent(SeeWkIntent.ShowBottomSheet(it))},
                    wkElement = it,
                    startPad = 10.dp,
                    fgColor = tertiary
                )
            }
        }
        if (state.bottomSheet && state.selectedExercise != null) {
            val sheetState = rememberModalBottomSheetState(
                skipPartiallyExpanded = false,
            )
            BottomSheet(
                sheetState = sheetState,
                onDismiss = {sendIntent(SeeWkIntent.HideBottomSheet)},
                content = { ExerciseInfo(state.selectedExercise) }
            )
        }
    }
}



@Composable
fun SeeWkFixedBanner(
    modifier: Modifier = Modifier,
    wkName: String,
    onGoBack: () -> Unit,
    onExecuteWk: () -> Unit,
) {

    val dpSideMargin = WkTemplateScreenConstants.sideMargin
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = dpSideMargin),
        verticalArrangement = Arrangement.Bottom
    ) {
        SeeWkBannerTopRow(
            modifier = Modifier.weight(1f),
            middleContent = {
                Text(
                    text = wkName,
                    color = title,
                    fontSize = 16.sp,
                    style = TitleTypo,
                )
            },
            onGoBack = onGoBack,
            onExecuteWk = onExecuteWk,
            executeButton = true,
        )
        WkTemplateViewLabels()
    }
}

@Composable
fun SeeWkBigBanner(
    wkName: String,
    wkDescription: String?,
    onGoBack: () -> Unit,
    onExecuteWk: () -> Unit,
    modifier: Modifier = Modifier,
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
            .padding(horizontal = dpSideMargin)
    ) {
        SeeWkBannerTopRow(
            modifier = Modifier
                .height(dpTopRow),
            onGoBack = onGoBack,
            onExecuteWk = onExecuteWk,
            middleContent = {
                Text(
                    text = wkName,
                    color = title,
                    fontSize = 18.sp,
                    style = TitleTypo,
                )
            }

        )
        BannerTitleRow(
            modifier = Modifier
                .height(dpTitle)
                .padding(bottom = dpInBetweenMargin),
            wkDescription = wkDescription
        )
        TagRow(
            modifier = Modifier
                .padding(bottom = dpInBetweenMargin)
                .height(dpTags)
        )
        ButtonsRow(
            modifier = Modifier
                .height(dpButtons)
                .padding(bottom = dpInBetweenMargin / 2),
            onEdit = {},
            onShare = {},
            onStats = {},
            onExecuteWk = {},
        )
        WkTemplateViewLabels(
            modifier = Modifier
                .height(dpLabels),
        )
    }
}

@Preview
@Composable
private fun SeeWkPreview() {
    ScreenBody(
        state = SeeWkState.initial.copy(
            wkTemplate = WORKOUT_TEMPLATE,
        ),
        sendIntent = {},
    )
}