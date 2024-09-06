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
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.QueryStats
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
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
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
import com.android.salamandra.destinations.ExecuteWkScreenDestination
import com.android.salamandra.ui.theme.TitleTypo
import com.android.salamandra.ui.theme.tertiary
import com.android.salamandra.ui.theme.title
import com.android.salamandra.workouts.executeWk.presentation.ExecuteWkNavArgs
import com.android.salamandra.workouts.seeWk.presentation.components.BannerTitleRow
import com.android.salamandra.workouts.seeWk.presentation.components.ButtonsRow
import com.android.salamandra._core.presentation.components.WkTemplateTopRow
import com.android.salamandra.workouts.seeWk.presentation.components.TagRow
import com.android.salamandra.R
import com.android.salamandra._core.presentation.components.NotImplented
import com.android.salamandra._core.presentation.components.TabRowBuilder
import com.android.salamandra._core.presentation.components.WkTemplateFixBanner
import com.android.salamandra.ui.theme.onTertiary
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
            SeeWkEvent.StartWk -> navigator.navigate(
                ExecuteWkScreenDestination(ExecuteWkNavArgs(wkTemplateId = state.wkTemplate.wkId))
            )

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
                WkTemplateFixBanner(
                    middleContent = {
                        Text(
                            text = state.wkTemplate.name,
                            style = TitleTypo,
                            fontSize = 18.sp,
                            color = title,
                            minLines = 1,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    },
                    onGoBack = { sendIntent(SeeWkIntent.NavigateUp) },
                    onActionButton = { sendIntent(SeeWkIntent.StartWk) },
                    modifier = Modifier
                        .height(fixedBannerHeight)
                        .background(mainColor),
                    actionIcon = Icons.Filled.PlayCircle
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
                    onExecuteWk = { sendIntent(SeeWkIntent.StartWk) },
                    onTitlePress = { sendIntent(SeeWkIntent.ShowTemplateInfo(0)) },
                    onStats = { sendIntent(SeeWkIntent.ShowTemplateInfo(1)) },
                    onEdit = { sendIntent(SeeWkIntent.ShowNotImplementedBanner) },
                    onShare = { sendIntent(SeeWkIntent.ShowNotImplementedBanner) },
                    onTag = { sendIntent(SeeWkIntent.ShowNotImplementedBanner) },
                )
                FadeLip()
                Spacer(modifier = Modifier.size(5.dp))
            }
            itemsIndexed(state.wkTemplate.elements) { index, element ->
                WkElementComponent(
                    onOption = { sendIntent(SeeWkIntent.ShowExerciseInfo(index)) },
                    wkElement = element,
                    startPad = 10.dp,
                    fgColor = tertiary
                )
            }
        }
        if (state.selectedElementIndex != null) {
            val sheetState = rememberModalBottomSheetState(
                skipPartiallyExpanded = false,
            )
            BottomSheet(
                sheetState = sheetState,
                onDismiss = { sendIntent(SeeWkIntent.HideBottomSheet) },
                content = { ExerciseInfo(state.wkTemplate.elements[state.selectedElementIndex].exercise) }
            )
        }
        else if (state.bottomSheetTab != null) {
            val sheetState = rememberModalBottomSheetState(
                skipPartiallyExpanded = false,
            )
            BottomSheet(
                sheetState = sheetState,
                onDismiss = { sendIntent(SeeWkIntent.HideBottomSheet) },
                content = {
                    TabRowBuilder(
                        contents = listOf({ NotImplented() }, { NotImplented() }),
                        icons = listOf(Icons.Outlined.Info, Icons.Outlined.QueryStats),
                        titles = listOf("Workout Information", "Stats and History"),
                        selectedTab = state.bottomSheetTab
                    )

                }
            )
        }
        else if (state.notImplementedBanner) {
            val sheetState = rememberModalBottomSheetState(
                skipPartiallyExpanded = false,
            )
            BottomSheet(
                sheetState = sheetState,
                onDismiss = { sendIntent(SeeWkIntent.HideBottomSheet) },
                content = { NotImplented() }
            )
        }


    }
}


@Composable
fun SeeWkBigBanner(
    wkName: String,
    wkDescription: String?,
    onGoBack: () -> Unit,
    onExecuteWk: () -> Unit,
    onTitlePress: () -> Unit,
    onShare: () -> Unit,
    onEdit: () -> Unit,
    onStats: () -> Unit,
    onTag: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val dpTopRow = WkTemplateScreenConstants.bannerRowHeights.top
    val dpTitle = WkTemplateScreenConstants.bannerRowHeights.picture
    val dpTags = WkTemplateScreenConstants.bannerRowHeights.tags
    val dpButtons = WkTemplateScreenConstants.bannerRowHeights.buttons
    val dpLabels = WkTemplateScreenConstants.bannerRowHeights.labels
    val dpMargins = WkTemplateScreenConstants.bannerRowHeights.margins

    val dpOutsideMargin = WkTemplateScreenConstants.outsideMargin
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = dpOutsideMargin)
            .padding(top = dpOutsideMargin)
    ) {
        WkTemplateTopRow(
            modifier = Modifier
                .height(dpTopRow),
            onGoBack = onGoBack,
            onActionButton = onExecuteWk,
            middleContent = {
                Text(
                    text = stringResource(R.string.workout_preview),
                    color = onTertiary,
                    fontSize = 16.sp,
                    style = TitleTypo,
                )
            },
            actionIcon = null

        )
        Spacer(Modifier.height(dpMargins[0]))
        BannerTitleRow(
            modifier = Modifier
                .height(dpTitle),
            wkDescription = wkDescription,
            wkName = wkName,
            onTitlePress = onTitlePress,
        )
        Spacer(Modifier.height(dpMargins[1]))
        TagRow(
            modifier = Modifier
                .height(dpTags),
            onClick = onTag
        )
        Spacer(Modifier.height(dpMargins[2]))
        ButtonsRow(
            modifier = Modifier
                .height(dpButtons),
            onEdit = onEdit,
            onShare = onShare,
            onStats = onStats,
            onExecuteWk = onExecuteWk,
        )
        Spacer(Modifier.height(dpMargins[3]))
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