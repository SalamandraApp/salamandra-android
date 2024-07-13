package com.android.salamandra.workouts.seeWk.presentation

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.outlined.ArrowBackIosNew
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.QueryStats
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import com.android.salamandra._core.presentation.components.BottomSheet
import com.android.salamandra._core.presentation.components.ExerciseInfo
import com.android.salamandra._core.presentation.components.FadeLip
import com.android.salamandra._core.presentation.components.WkElementComponent
import com.android.salamandra._core.presentation.components.WkTemplatePicture
import com.android.salamandra._core.presentation.components.WkTemplateViewLabels
import com.android.salamandra._core.util.WORKOUT_TEMPLATE
import com.android.salamandra.ui.theme.NormalTypo
import com.android.salamandra.ui.theme.SemiTypo
import com.android.salamandra.ui.theme.TitleTypo
import com.android.salamandra.ui.theme.onTertiary
import com.android.salamandra.ui.theme.primaryVariant
import com.android.salamandra.ui.theme.secondary
import com.android.salamandra.ui.theme.tertiary
import com.android.salamandra.ui.theme.title
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
    val bannerHeight = 320.dp
    val fixedBannerHeight = 85.dp

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
    val columnWeights: FloatArray = floatArrayOf(0.5f, 0.1f, 0.1f, 0.15f, 0.1f)
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
                    modifier = Modifier
                        .height(fixedBannerHeight),
                    columnWeightVector = columnWeights,
                    state = state,
                    sendIntent = sendIntent,
                    bgColor = tertiary
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

            val startPad = 15.dp
            item {
                SeeWkBigBanner(
                    modifier = Modifier.height(bannerHeight),
                    state = state,
                    sendIntent = sendIntent,
                    bgColor = mainColor,
                    columnWeightVector = columnWeights
                )
                FadeLip()
                Spacer(modifier = Modifier.size(5.dp))
            }
            items(state.wkTemplate.elements) {
                WkElementComponent(
                    onOption = {sendIntent(SeeWkIntent.ShowBottomSheet)},
                    wkElement = it,
                    fgColor = tertiary,
                    columnWeightVector = columnWeights,
                    startPad = 10.dp
                )
            }
        }
        if (state.bottomSheet) {
            val sheetState = rememberModalBottomSheetState(
                skipPartiallyExpanded = false,
            )
            BottomSheet(
                sheetState = sheetState,
                onDismiss = {sendIntent(SeeWkIntent.HideBottomSheet)},
                content = { ExerciseInfo() }
            )
        }
    }
}



@Composable
fun SeeWkFixedBanner(
    state: SeeWkState,
    sendIntent: (SeeWkIntent) -> Unit,
    modifier: Modifier = Modifier,
    columnWeightVector: FloatArray,
    bgColor: Color) {

    val dpSideMargin = 20.dp
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(bgColor)
            .padding(horizontal = dpSideMargin),
        verticalArrangement = Arrangement.Bottom
    ) {
        SeeWkBannerTopRow(
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
            },
            executeButton = true,
        )
        WkTemplateViewLabels(
            columnWeightVector = columnWeightVector
        )
    }
}

@Composable
fun SeeWkBigBanner(
    modifier: Modifier = Modifier,
    state: SeeWkState,
    sendIntent: (SeeWkIntent) -> Unit,
    columnWeightVector: FloatArray,
    bgColor: Color) {
    if (columnWeightVector.size != 5) {
        throw IllegalArgumentException("The length of the float array must be 5.")
    }
    val wTopRow     = 180f
    val wTitle      = 500f
    val wTags       = 150f
    val wButtons    = 180f
    val wLabels     = 100f

    val dpSideMargin = 20.dp
    val dpInBetweenMargin = 15.dp
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(bgColor)
            .padding(horizontal = dpSideMargin)
    ) {
        SeeWkBannerTopRow(
            modifier = Modifier
                .weight(wTopRow),
            sendIntent = sendIntent,
            state = state,
            middleContent = {
                Text(
                    text = state.wkTemplate.name,
                    color = title,
                    fontSize = 18.sp,
                    style = TitleTypo,
                )
            }

        )
        BannerTitleRow(
            modifier = Modifier
                .weight(wTitle)
                .padding(bottom = dpInBetweenMargin),
            sendIntent = sendIntent,
            state = state
        )
        EditTagRow(
            modifier = Modifier
                .padding(bottom = dpInBetweenMargin)
                .weight(wTags),
            state = state,
            sendIntent = sendIntent
        )
        ButtonsRowBanner(
            modifier = Modifier
                .weight(wButtons)
                .padding(bottom = dpInBetweenMargin / 2),
            state = state,
            sendIntent = sendIntent
        )
        WkTemplateViewLabels(
            modifier = Modifier
                .weight(wLabels),
            columnWeightVector = columnWeightVector,
        )
    }
}

@Composable
private fun ButtonsRowBanner (
    modifier: Modifier = Modifier,
    state: SeeWkState,
    sendIntent: (SeeWkIntent) -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    )
    {

        Box(modifier = Modifier
            .clickable {  }
        ) {
            Icon(
                imageVector = Icons.Outlined.Edit,
                tint = onTertiary,
                contentDescription = "Edit workout"
            )
        }
        Box(
            modifier = Modifier
                .padding(start = 20.dp)
                .clickable { sendIntent(SeeWkIntent.ShowBottomSheet) }
        ) {
            Icon(
                imageVector = Icons.Filled.Share,
                tint = onTertiary,
                contentDescription = "Share workout"
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        ExtendedFloatingActionButton(
            containerColor = secondary,
            contentColor = primaryVariant,
            elevation = FloatingActionButtonDefaults.elevation(8.dp),
            onClick = {  }) {
            Icon(
                imageVector = Icons.Outlined.QueryStats,
                contentDescription = "Wk Stats",
            )
            Text(
                text = "Stats",
                modifier = Modifier.padding(start = 8.dp)
            )
        }
        FloatingActionButton(
            modifier = Modifier.padding(start = 30.dp),
            containerColor = primaryVariant.copy(0.3f),
            contentColor = primaryVariant,
            elevation = FloatingActionButtonDefaults.elevation(8.dp),
            onClick = { }) {
            Icon(
                imageVector = Icons.Filled.PlayCircle,
                contentDescription = "Execute Workout",
            )
        }
    }
}

@Composable
private fun BannerTitleRow(
    modifier: Modifier = Modifier,
    sendIntent: (SeeWkIntent) -> Unit,
    state: SeeWkState
) {
    Row (
        modifier = modifier
            .fillMaxWidth()
    ) {
        Column (
            modifier = Modifier
                .fillMaxHeight()
                .weight(1f)
                .padding(end = 20.dp)
        ) {
            val descriptionPlaceholder =
                "Really really really long description, I mean really long, why do you need so much, a little too much"
            val textToShow = state.wkTemplate.description ?: descriptionPlaceholder
            Text(
                modifier = Modifier.padding(top = 20.dp),
                text = textToShow,
                style = TitleTypo,
                fontSize = 14.sp,
                color = onTertiary,
                minLines = 3,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.weight(1f))
            val iconColor = onTertiary.copy(0.7f)
            Row (
                modifier = Modifier.height(50.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Outlined.CalendarMonth,
                    contentDescription = "Delete Wk",
                    tint = iconColor,
                )
                Text(
                    modifier = Modifier.padding(start = 5.dp),
                    text = "13 days since last time",
                    color = iconColor,
                    style = NormalTypo,
                    fontSize = 14.sp
                )
            }
        }
        Column (
            modifier = Modifier
                .fillMaxHeight()
        ) {
            WkTemplatePicture(
                modifier = Modifier
                    .fillMaxHeight()
                    .aspectRatio(1f),
                shape = RoundedCornerShape(5)
            )
        }
    }
}

@Composable
private fun SeeWkBannerTopRow(
    modifier: Modifier = Modifier,
    sendIntent: (SeeWkIntent) -> Unit,
    state: SeeWkState,
    middleContent: @Composable () -> Unit,
    executeButton: Boolean = false
) {
    Row(
        modifier = modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    )
    {
        Box(modifier = Modifier
            .clickable { sendIntent(SeeWkIntent.NavigateUp) }
        ) {
            Icon(
                imageVector = Icons.Outlined.ArrowBackIosNew,
                tint = onTertiary,
                contentDescription = "Search workout"
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        middleContent()
        Spacer(modifier = Modifier.weight(1f))
        if (executeButton)
            FloatingActionButton(
                modifier = Modifier
                    .padding(vertical = 8.dp),
                containerColor = primaryVariant.copy(0.3f),
                contentColor = primaryVariant,
                elevation = FloatingActionButtonDefaults.elevation(8.dp),
                onClick = { }) {
                Icon(
                    imageVector = Icons.Filled.PlayCircle,
                    contentDescription = "Execute Workout",
                )
            }
    }
}

@Composable
private fun EditTagRow(
    modifier: Modifier = Modifier,
    sendIntent: (SeeWkIntent) -> Unit,
    state: SeeWkState
) {
    Row(
        modifier = modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        val wTags = 800f
        val wButton = 200f
        Box(
            modifier = Modifier
                .weight(wTags)
                .padding(end = 15.dp)
        ) {
            val bgColor = onTertiary.copy(0.4f)
            val items = listOf("#mytag1", "#tag2", "#thistag3", "#tag4")
            LazyRow {
                items(items.size) { index ->
                    Row(
                        modifier = Modifier
                            .fillMaxHeight()
                            .padding(end = if (index == items.size - 1) 0.dp else 10.dp)
                            .background(bgColor, shape = RoundedCornerShape(50)),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {

                        Text(
                            modifier = Modifier.padding(horizontal = 8.dp),
                            text = items[index],
                            color = tertiary,
                            style = SemiTypo,
                            fontSize = 13.sp
                        )

                    }
                }
            }
            if (items.size >= 5)
                FadeLip(vertical = true, modifier = Modifier.align(Alignment.CenterEnd))
        }

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

@Preview
@Composable
private fun SeeWkFixedBannerPreview() {

    val columnWeights: FloatArray = floatArrayOf(0.5f, 0.1f, 0.1f, 0.15f, 0.1f)
    SeeWkFixedBanner(
        modifier = Modifier.height(70.dp),
        state = SeeWkState.initial.copy(wkTemplate = WORKOUT_TEMPLATE),
        sendIntent = {},
        columnWeightVector = columnWeights,
        bgColor = tertiary
    )
}
