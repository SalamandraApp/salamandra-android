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
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.FitnessCenter
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
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
import com.android.salamandra._core.presentation.components.NotImplented
import com.android.salamandra._core.presentation.components.TabRowBuilder
import com.android.salamandra._core.presentation.components.WkTemplateFixBanner
import com.android.salamandra._core.presentation.components.WkTemplateTopRow
import com.android.salamandra._core.util.WORKOUT_TEMPLATE
import com.android.salamandra.destinations.HomeScreenDestination
import com.android.salamandra.destinations.SearchScreenDestination
import com.android.salamandra.ui.theme.TitleTypo
import com.android.salamandra.ui.theme.onTertiary
import com.android.salamandra.ui.theme.secondary
import com.android.salamandra.ui.theme.tertiary
import com.android.salamandra.workouts.commons.presentation.components.WkTemplateElement
import com.android.salamandra.workouts.commons.presentation.components.WkTemplateViewLabels
import com.android.salamandra.workouts.commons.presentation.constants.WkTemplateScreenConstants
import com.android.salamandra.workouts.editWk.presentation.components.BannerTitleRow
import com.android.salamandra.workouts.editWk.presentation.components.ButtonsRowBanner
import com.android.salamandra.workouts.editWk.presentation.components.EditWkTemplateElement
import com.android.salamandra.workouts.editWk.presentation.components.EditTagRow
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Destination(navArgsDelegate = EditWkNavArgs::class)
@Composable
fun EditWkScreen(navigator: DestinationsNavigator, viewModel: EditWkViewModel = hiltViewModel()) {
    val state by viewModel.state.collectAsState()
    val events by viewModel.events.collectAsState(initial = null)
    LaunchedEffect(events) {
        when (events) {
            EditWkEvent.NavigateToHome -> navigator.navigate(HomeScreenDestination)
            EditWkEvent.NavigateToSearch -> navigator.navigate(SearchScreenDestination())
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
                WkTemplateFixBanner (
                    middleContent = {
                        Text(
                            text = stringResource(R.string.edit_workout),
                            color = onTertiary,
                            fontSize = 18.sp,
                            style = TitleTypo,
                        )
                    },
                    modifier = Modifier
                        .height(fixedBannerHeight)
                        .background(mainColor),
                    onActionButton = { sendIntent(EditWkIntent.CreateWorkout) },
                    onGoBack = { sendIntent(EditWkIntent.NavigateToHome) },
                    actionIcon = Icons.Filled.CheckCircle,
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

            val notImplemented = { sendIntent(EditWkIntent.ShowNotImplementedBanner) }
            item {
                EditWkBigBanner(
                    modifier = Modifier.height(bannerHeight),
                    wkName = state.wkTemplate.name,
                    wkDescription = state.wkTemplate.description,
                    bgColor = mainColor,
                    onClose = { sendIntent(EditWkIntent.NavigateToHome) },
                    onSave =  { sendIntent(EditWkIntent.CreateWorkout) },
                    onDeleteWk = notImplemented,
                    onAddTag = notImplemented,
                    onDeleteTag = notImplemented,
                    onEditTag = notImplemented,
                    onAddExercise = { sendIntent(EditWkIntent.NavigateToSearch) },
                    onChangeName = { sendIntent(EditWkIntent.ChangeWkName(it)) },
                    onChangeDescription = { sendIntent(EditWkIntent.ChangeWkDescription(it)) },
                )
                FadeLip()
                Spacer(modifier = Modifier.size(8.dp))
            }
            itemsIndexed(state.wkTemplate.elements) { index, element ->
                WkTemplateElement(
                    onOption = { sendIntent(EditWkIntent.ShowElementBanner(index, it)) },
                    wkElement = element,
                    fgColor = secondary,
                )
                Spacer(Modifier.height(15.dp))
            }
        }
        if (state.selectedElementIndex != null && state.textFieldSelected != null) {
            val selectedElement = state.wkTemplate.elements[state.selectedElementIndex]
            val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = false)

            BottomSheet(
                sheetState = sheetState,
                onDismiss = { sendIntent(EditWkIntent.HideBottomSheet) },
                content = {
                    TabRowBuilder(
                        contents = listOf(
                            {
                                EditWkTemplateElement(
                                    element = selectedElement,
                                    fieldSelected = state.textFieldSelected,
                                    onEditSets = { newSets -> sendIntent(EditWkIntent.ChangeSets(newSets)) },
                                    onEditReps= { newReps -> sendIntent(EditWkIntent.ChangeReps(newReps)) },
                                    onEditWeight = { newWeight -> sendIntent(EditWkIntent.ChangeWeight(newWeight)) },
                                    onEditRest = { newRest -> sendIntent(EditWkIntent.ChangeRest(newRest)) },
                                    onDeleteElement = { sendIntent(EditWkIntent.DeleteWkElement) },
                                )
                            },
                            { ExerciseInfo(selectedElement.exercise) }
                        ),
                        icons = listOf(Icons.Outlined.Edit, Icons.Outlined.FitnessCenter),
                        titles = listOf("Edit", "Info")
                    )
                }
            )
        } else if (state.notImplementedBanner) {
            val sheetState = rememberModalBottomSheetState(
                skipPartiallyExpanded = false,
            )
            BottomSheet(
                sheetState = sheetState,
                onDismiss = { sendIntent(EditWkIntent.HideBottomSheet) },
                content = { NotImplented() }
            )
        }
    }
}


@Composable
fun EditWkBigBanner(
    modifier: Modifier = Modifier,
    wkName: String,
    wkDescription: String?,
    onSave: () -> Unit,
    onClose: () -> Unit,
    onDeleteWk: () -> Unit,
    onAddExercise: () -> Unit,
    onAddTag: () -> Unit,
    onDeleteTag: () -> Unit,
    onEditTag: () -> Unit,
    onChangeName: (String) -> Unit,
    onChangeDescription: (String) -> Unit,
    bgColor: Color
) {

    val dpTopRow     = WkTemplateScreenConstants.bannerRowHeights.top
    val dpTitle      = WkTemplateScreenConstants.bannerRowHeights.picture
    val dpTags       = WkTemplateScreenConstants.bannerRowHeights.tags
    val dpButtons    = WkTemplateScreenConstants.bannerRowHeights.buttons
    val dpLabels     = WkTemplateScreenConstants.bannerRowHeights.labels
    val dpMargins    = WkTemplateScreenConstants.bannerRowHeights.margins

    val dpOutsideMargin = WkTemplateScreenConstants.outsideMargin
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(bgColor)
            .padding(horizontal = dpOutsideMargin)
            .padding(top = dpOutsideMargin)
    ) {
        WkTemplateTopRow(
            modifier = Modifier
                .height(dpTopRow),
            onGoBack = onClose,
            onActionButton = {},
            middleContent = {
                Text(
                    text = stringResource(R.string.edit_workout),
                    color = onTertiary,
                    fontSize = 16.sp,
                    style = TitleTypo,
                )
            },
            actionIcon = null
        )
        Spacer(Modifier.height(dpMargins[0]))
        BannerTitleRow(
            modifier = Modifier.height(dpTitle),
            wkName = wkName,
            wkDescription = wkDescription,
            onChangeName = onChangeName,
            onChangeDescription = onChangeDescription,
        )
        Spacer(Modifier.height(dpMargins[1]))
        EditTagRow(
            modifier = Modifier.height(dpTags),
            onAddTag = onAddTag,
            onDeleteTag = onDeleteTag,
            onEditTag = onEditTag,
        )
        Spacer(Modifier.height(dpMargins[2]))
        ButtonsRowBanner(
            modifier = Modifier.height(dpButtons),
            onAddExercise = onAddExercise,
            onDeleteWk = onDeleteWk,
            onSave = onSave
        )
        Spacer(Modifier.height(dpMargins[3]))
        WkTemplateViewLabels(
            modifier = Modifier.height(dpLabels),
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
