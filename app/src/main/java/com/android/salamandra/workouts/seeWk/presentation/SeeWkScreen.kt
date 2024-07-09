package com.android.salamandra.workouts.seeWk.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.outlined.ArrowBackIosNew
import androidx.compose.material.icons.outlined.BarChart
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.DragIndicator
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.PlayCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import com.android.salamandra._core.domain.model.workout.WkTemplateElement
import com.android.salamandra._core.presentation.components.FadeLip
import com.android.salamandra._core.presentation.components.MyRow
import com.android.salamandra._core.presentation.components.WkTemplatePicture
import com.android.salamandra._core.presentation.components.WkTemplateViewLabels
import com.android.salamandra._core.util.WORKOUT_TEMPLATE
import com.android.salamandra.ui.theme.NormalTypo
import com.android.salamandra.ui.theme.SemiTypo
import com.android.salamandra.ui.theme.TitleTypo
import com.android.salamandra.ui.theme.colorMessage
import com.android.salamandra.ui.theme.onSecondaryVariant
import com.android.salamandra.ui.theme.onTertiary
import com.android.salamandra.ui.theme.primaryVariant
import com.android.salamandra.ui.theme.secondary
import com.android.salamandra.ui.theme.secondaryVariant
import com.android.salamandra.ui.theme.tertiary
import com.android.salamandra.ui.theme.title
import com.android.salamandra.workouts.editWk.presentation.EditWkIntent
import com.android.salamandra.workouts.editWk.presentation.EditWkState
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator


@Destination(navArgsDelegate = SeeWkNavArgs::class)
@Composable
fun SeeWkScreen(navigator: DestinationsNavigator, viewModel: SeeWkViewModel = hiltViewModel()) {
    val state by viewModel.state.collectAsState()
    val events by viewModel.events.collectAsState(initial = null)
    LaunchedEffect(events) {
        when (events) {
            SeeWkEvent.NavigateBack -> navigator.navigateUp()
            // TODO: navigate to EditWk
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
    state: SeeWkState,
    sendIntent: (SeeWkIntent) -> Unit
) {
    val mainColor = tertiary
    val bannerHeight = 240.dp
    val fixedBannerHeight = 80.dp

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
    ) {
        if (scrolledPast.value) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.TopCenter)
                    .zIndex(1f),
            ) {
                SeeWkFixedBanner(
                    modifier = Modifier.height(fixedBannerHeight),
                    columnWeightVector = columnWeights,
                    state = state,
                    sendIntent = sendIntent,
                    backgroundColor = tertiary
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

            val startPad = 20.dp
            item {
                SeeWkBigBanner(
                    modifier = Modifier.height(bannerHeight),
                    state = state,
                    sendIntent = sendIntent,
                    backgroundColor = mainColor,
                    columnWeightVector = columnWeights
                )
                FadeLip()
                Spacer(modifier = Modifier.size(5.dp))
            }
            items(state.wkTemplate.elements) {
                SeeWkElementComponent(
                    wkElement = it,
                    fgColor = tertiary,
                    startPad = startPad,
                    columnWeightVector = columnWeights
                )
            }
        }
    }
}

@Composable
fun SeeWkBigBanner(
    modifier: Modifier = Modifier,
    state: SeeWkState,
    sendIntent: (SeeWkIntent) -> Unit,
    columnWeightVector: FloatArray,
    backgroundColor: Color
) {

    if (columnWeightVector.size != 5) {
        throw IllegalArgumentException("The length of the float array must be 5.")
    }
    val wTopSection = 800f
    val wPlaceholder = 200f

    val dpOutMargin = 20.dp

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(backgroundColor)
    ) {
        BigBannerTop(
            modifier = Modifier
                .weight(wTopSection)
                .fillMaxWidth(),
            sendIntent = sendIntent,
            state = state,
            backgroundColor = backgroundColor,
            dpOutMargin = dpOutMargin
        )
        BigBannerBottom(
            modifier = Modifier
                .weight(wPlaceholder)
                .padding(horizontal = dpOutMargin - 8.dp),
            sendIntent = sendIntent,
            columnWeightVector = columnWeightVector,
        )
    }
}

@Composable
fun BigBannerBottom(
    modifier: Modifier = Modifier,
    sendIntent: (SeeWkIntent) -> Unit,
    columnWeightVector: FloatArray,
) {
    Row (
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ){
        IconButton(
            onClick = { },
        ) {
            Icon(
                imageVector = Icons.Filled.Edit,
                tint = colorMessage,
                contentDescription = "Edit Template"
            )
        }

        IconButton(
            onClick = { },
        ) {
            Icon(
                imageVector = Icons.Filled.Share,
                tint = colorMessage,
                contentDescription = "Share Template"
            )
        }
        TextButton(
            onClick = { }) {
            MyRow {
                Icon(
                    imageVector = Icons.Outlined.BarChart,
                    tint = colorMessage,
                    contentDescription = "Go to monitoring"
                )
            }
            Text(
                text = "Workout Stats",
                color = colorMessage,
                modifier = Modifier.padding(start = 8.dp)
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        IconButton(
            onClick = { },
        ) {
            Icon(
                modifier = Modifier.size(40.dp),
                imageVector = Icons.Outlined.PlayCircle,
                tint = colorMessage,
                // tint = primaryVariant,
                contentDescription = "Execute Template"
            )
        }

    }
    Spacer(modifier = Modifier.height(12.dp))
    WkTemplateViewLabels(columnWeightVector = columnWeightVector)
}

@Composable
fun BigBannerTop(
    modifier: Modifier = Modifier,
    sendIntent: (SeeWkIntent) -> Unit,
    state: SeeWkState,
    backgroundColor: Color,
    dpOutMargin: Dp,
) {

    Row (
        modifier = modifier
    ) {
        val pictureSection = 370f
        val textSection = 630f
        Column (
            modifier = Modifier
                .weight(textSection)
                .fillMaxHeight()
        ) {
            Spacer(modifier = Modifier.height(dpOutMargin))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = dpOutMargin - 15.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = { sendIntent(SeeWkIntent.NavigateBack) },
                ) {
                    Icon(
                        imageVector = Icons.Outlined.ArrowBackIosNew,
                        tint = onTertiary,
                        contentDescription = "Close screen"
                    )
                }
                Text(
                    modifier = Modifier
                        .padding(horizontal = 5.dp)
                        .weight(1f),
                    text = state.wkTemplate.name,
                    style = TitleTypo.copy(color = title, fontSize = 18.sp),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            Row(
                modifier = Modifier
                    .padding(start = dpOutMargin, end = dpOutMargin, top = 7.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    modifier = Modifier
                        .weight(1f),
                    text = "Placeholder text for template description (max 2 lines) more text mote tet",
                    style = SemiTypo.copy(color = onTertiary, fontSize = 12.sp),
                    minLines = 2,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
            TagRow(modifier = Modifier
                .fillMaxWidth()
                .height(40.dp)
                .padding(start = dpOutMargin, end = dpOutMargin, top = 7.dp),
                textColor = backgroundColor
            )
        }
        Column (
            modifier = Modifier
                .weight(pictureSection)
                .fillMaxHeight(),
            // verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            WkTemplatePicture(
                modifier = Modifier
                    .padding(top = dpOutMargin, end = dpOutMargin)
                    .aspectRatio(1f),
                shape = RoundedCornerShape(5),
            )
        }
    }
}


@Composable
fun TagRow (
    modifier: Modifier = Modifier,
    textColor: Color,
) {
    Row (
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            contentAlignment = Alignment.CenterEnd
        ) {
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                val items = listOf("#mytag1", "#tag2", "#thistag3", "#tag4")
                items(items.size) { index ->
                    Box(
                        modifier = Modifier
                            .padding(end = 5.dp)
                            .clip(RoundedCornerShape(40.dp))
                            .background(colorMessage)
                            // .background(onTertiary.copy(0.5f))
                    ) {
                        Text(
                            modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp),
                            text = items[index], style = SemiTypo.copy(color = textColor, fontSize = 12.sp)
                        )
                    }
                }
            }
            FadeLip(vertical = true, modifier = Modifier.align(Alignment.CenterEnd))
        }
    }
}



@Composable
fun SeeWkFixedBanner(
    state: SeeWkState,
    sendIntent: (SeeWkIntent) -> Unit,
    modifier: Modifier = Modifier,
    columnWeightVector: FloatArray,
    backgroundColor: Color
) {
    val topWeight =     180f
    val labelWeight =   100f
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(backgroundColor)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .weight(topWeight)
                .align(Alignment.Start),
            verticalAlignment = Alignment.CenterVertically
        )
        {
            IconButton(
                onClick = {/*TODO*/ },
                modifier = Modifier.padding(10.dp)
            ) {
                Icon(
                    imageVector = Icons.Outlined.ArrowBackIosNew,
                    tint = onTertiary,
                    contentDescription = "Close screen"
                )
            }
            Spacer(modifier = Modifier.weight(1f))

            BasicTextField(
                modifier = Modifier
                    .padding(start = 12.dp)
                    .padding(vertical = 6.dp),
                singleLine = true,
                enabled = false,
                value = state.wkTemplate.name,
                textStyle = TitleTypo.copy(color = title, fontSize = 18.sp),
                onValueChange = { }
            )


            Spacer(modifier = Modifier.weight(1f))
            IconButton(
                onClick = {/*TODO*/ },
                modifier = Modifier.padding(10.dp)
            ) {
                Icon(
                    modifier = Modifier.size(30.dp),
                    imageVector = Icons.Outlined.PlayCircle,
                    tint = primaryVariant,
                    contentDescription = "Execute template"
                )
            }
        }
        WkTemplateViewLabels(
            modifier = Modifier
                .weight(labelWeight)
                .align(Alignment.Start),
            columnWeightVector = columnWeightVector,
        )
    }
}

@Composable
fun SeeWkElementComponent(
    modifier: Modifier = Modifier,
    wkElement: WkTemplateElement,
    columnWeightVector: FloatArray,
    startPad: Dp,
    fgColor: Color
) {

    if (columnWeightVector.size != 5) {
        throw IllegalArgumentException("The length of the float array must be 5.")
    }
    val nameColor = onSecondaryVariant
    val valuesColor = onSecondaryVariant
    val valueStyle = NormalTypo.copy(
        color = valuesColor,
        textAlign = TextAlign.Center,
    )
    val valueBoxColor = secondaryVariant
    val valueBoxSize = 17.dp

    Row(
        modifier = modifier
            .height(50.dp)
            .fillMaxWidth()
            .background(fgColor),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // EXERCISE NAME
        Text(
            modifier = Modifier
                .weight(columnWeightVector[0])
                .padding(start = startPad),
            text = wkElement.exercise.name,
            style = SemiTypo,
            color = nameColor,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
        val elements = listOf(
            wkElement.sets.toString(),
            wkElement.reps.toString(),
            wkElement.weight.toString()
        )
        val columnWeights = listOf(
            columnWeightVector[1],
            columnWeightVector[2],
            columnWeightVector[3]
        )
        val onValueChangeHandlers = listOf<(String) -> Unit>(
            { if (it.all { char -> char.isDigit() }) { /*TODO: Handle sets change*/ } },
            { if (it.all { char -> char.isDigit() }) { /*TODO: Handle reps change*/ } },
            { if (it.all { char -> char.isDigit() }) { /*TODO: Handle weight change*/ } }
        )

        elements.forEachIndexed { index, value ->
            Box(
                modifier = Modifier
                    .weight(columnWeights[index])
                    .padding(end = 5.dp),
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    BasicTextField(
                        singleLine = true,
                        enabled = false,
                        value = value,
                        textStyle = valueStyle,
                        onValueChange = onValueChangeHandlers[index],
                        keyboardOptions = KeyboardOptions.Default.copy(
                            keyboardType = KeyboardType.Number
                        )
                    )
                }
            }
        }

        Box (modifier = Modifier.weight(columnWeightVector[4])) {
            IconButton(onClick = { /*TODO*/ }) {
                Icon(
                    modifier = Modifier.size(18.dp),
                    imageVector = Icons.Outlined.Info,
                    contentDescription = "Move Exercise",
                    // tint = onTertiary.copy(alpha = 0.5f),
                    tint = colorMessage
                )
            }
        }
    }
}

@Preview()
@Composable
private fun SeeWkPreview() {
    ScreenBody(
        state = SeeWkState.initial.copy(
            wkTemplate = WORKOUT_TEMPLATE,
        ),
        sendIntent = {}
    )
}


@Preview()
@Composable
fun FixedBannerPreview() {
    val columnWeights: FloatArray = floatArrayOf(0.5f, 0.1f, 0.1f, 0.15f, 0.1f)

    SeeWkFixedBanner(
        modifier = Modifier.height(80.dp),
        state = SeeWkState.initial,
        sendIntent = {},
        columnWeightVector = columnWeights,
        backgroundColor = tertiary
    )
}


