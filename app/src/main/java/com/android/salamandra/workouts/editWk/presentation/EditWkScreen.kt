package com.android.salamandra.workouts.editWk.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Construction
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.DragIndicator
import androidx.compose.material.icons.outlined.PersonOff
import androidx.compose.material.icons.outlined.Share
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import com.android.salamandra.R
import com.android.salamandra._core.domain.model.workout.WkTemplateElement
import com.android.salamandra._core.presentation.components.FadeLip
import com.android.salamandra._core.presentation.components.MyRow
import com.android.salamandra._core.presentation.components.WkTemplatePicture
import com.android.salamandra._core.presentation.components.WkTemplateViewLabels
import com.android.salamandra._core.util.WORKOUT_TEMPLATE
import com.android.salamandra.destinations.HomeScreenDestination
import com.android.salamandra.ui.theme.NormalTypo
import com.android.salamandra.ui.theme.SalamandraTheme
import com.android.salamandra.ui.theme.SemiTypo
import com.android.salamandra.ui.theme.TitleTypo
import com.android.salamandra.ui.theme.colorError
import com.android.salamandra.ui.theme.colorMessage
import com.android.salamandra.ui.theme.onSecondaryVariant
import com.android.salamandra.ui.theme.onTertiary
import com.android.salamandra.ui.theme.primaryVariant
import com.android.salamandra.ui.theme.secondary
import com.android.salamandra.ui.theme.secondaryVariant
import com.android.salamandra.ui.theme.tertiary
import com.android.salamandra.ui.theme.title
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
    state: EditWkState,
    sendIntent: (EditWkIntent) -> Unit,
) {
    val mainColor = tertiary
    val bannerHeight = 320.dp
    val fixedBannerHeight = 100.dp

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
            Column (
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.TopCenter)
                    .zIndex(1f),
            ){
                EditWkFixedBanner(
                    modifier = Modifier
                        .height(fixedBannerHeight),
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

            val startPad = 15.dp
            item {
                EditWkBigBanner(
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
                EditWkElementComponent(
                    wkElement = it,
                    fgColor = secondary,
                    startPad = startPad,
                    columnWeightVector = columnWeights
                )
            }
        }
    }
}

@Composable
fun EditWkFixedBanner(
    state: EditWkState,
    sendIntent: (EditWkIntent) -> Unit,
    modifier: Modifier = Modifier,
    columnWeightVector: FloatArray,
    backgroundColor: Color) {
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
                    imageVector = Icons.Outlined.Close,
                    tint = onTertiary,
                    contentDescription = "Close screen"
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(10.dp))
                    .background(secondary)
            )
            {
                BasicTextField(
                    modifier = Modifier
                        .padding(start = 12.dp)
                        .padding(vertical = 6.dp),
                    singleLine = true,
                    enabled = true,
                    value = state.wkTemplate.name,
                    textStyle = TitleTypo.copy(color = title, fontSize = 20.sp),
                    onValueChange = { sendIntent(EditWkIntent.ChangeWkName(it)) }
                )
            }


            Spacer(modifier = Modifier.weight(1f))
            IconButton(
                onClick = {/*TODO*/ },
                modifier = Modifier.padding(10.dp)
            ) {
                Icon(
                    modifier = Modifier.size(30.dp),
                    imageVector = Icons.Outlined.CheckCircle,
                    tint = primaryVariant,
                    contentDescription = "Search workout"
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
fun EditWkBigBanner(
    modifier: Modifier = Modifier,
    state: EditWkState,
    sendIntent: (EditWkIntent) -> Unit,
    columnWeightVector: FloatArray,
    backgroundColor: Color) {
    if (columnWeightVector.size != 5) {
        throw IllegalArgumentException("The length of the float array must be 5.")
    }
    val topWeight =     175f
    val titleWeight =   400f
    val tagWeight =     125f
    val buttonsWeight = 200f
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
                    imageVector = Icons.Outlined.Close,
                    tint = onTertiary,
                    contentDescription = "Search workout"
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = stringResource(R.string.edit_workout),
                color = onTertiary,
                fontSize = 16.sp,
                style = TitleTypo,
            )
            Spacer(modifier = Modifier.weight(1f))
            IconButton(
                onClick = {/*TODO*/ },
                modifier = Modifier.padding(10.dp)
            ) {
                Icon(
                    modifier = Modifier.size(30.dp),
                    imageVector = Icons.Outlined.CheckCircle,
                    tint = primaryVariant,
                    contentDescription = "Search workout"
                )
            }
        }
        val sideMargin = 20
        val topMargin = sideMargin / 2;
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .weight(titleWeight)
                .align(Alignment.Start),
        )
        {
            val textWeight = 0.6f
            val picWeight = 0.5f
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(textWeight),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.Start
            ) {
                Box(
                    modifier = Modifier
                        .padding(horizontal = sideMargin.dp, vertical = topMargin.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(secondary)
                        .fillMaxWidth()
                )
                {
                    BasicTextField(
                        modifier = Modifier
                            .padding(start = 12.dp)
                            .padding(vertical = 6.dp),
                        singleLine = true,
                        enabled = true,
                        value = state.wkTemplate.name,
                        textStyle = TitleTypo.copy(color = title, fontSize = 20.sp),
                        onValueChange = { sendIntent(EditWkIntent.ChangeWkName(it)) }
                    )
                }
                val description = state.wkTemplate.description
                val textToShow = description ?: "Description..."
                val textColor =
                    if (description.isNullOrEmpty()) title.copy(alpha = 0.5f) else title
                Box(
                    modifier = Modifier
                        .padding(horizontal = sideMargin.dp)
                        .padding(bottom = topMargin.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(secondary)
                        .fillMaxWidth()
                        //.fillMaxHeight()

                ) {
                    BasicTextField(
                        modifier = Modifier
                            .padding(12.dp),
                        singleLine = true,
                        enabled = true,
                        value = textToShow,
                        textStyle = TitleTypo.copy(color = textColor, fontSize = 14.sp),
                        onValueChange = { sendIntent(EditWkIntent.ChangeWkName(it)) }
                    )
                }
                Row(
                    modifier = Modifier
                        .weight(tagWeight)
                        .fillMaxWidth()
                        .padding(horizontal = sideMargin.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Construction,
                        contentDescription = "Construction icon",
                        modifier = Modifier.size(20.dp),
                        tint = onTertiary,
                    )
                    Text(
                        text = "WIP",
                        color = onTertiary,
                        fontSize = 18.sp,
                        modifier = Modifier.padding(start = 8.dp),
                        style = TitleTypo,
                    )
                }
            }
            Column(
                modifier = Modifier
                    .weight(picWeight)
                    .padding(horizontal = sideMargin.dp)
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                WkTemplatePicture(
                    modifier = Modifier.aspectRatio(1f),
                    shape = RoundedCornerShape(5)
                )
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .weight(tagWeight)
                .align(Alignment.End),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Outlined.Construction,
                contentDescription = "Construction icon",
                modifier = Modifier.size(20.dp),
                tint = colorMessage,
            )
            Text(
                text = "WIP (tags)",
                color = colorMessage,
                fontSize = 18.sp,
                modifier = Modifier.padding(start = 8.dp),
                style = TitleTypo,
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .weight(buttonsWeight)
                .align(Alignment.Start),
            verticalAlignment = Alignment.CenterVertically
        )
        {
            MyRow(
                modifier = Modifier.clickable
                {
                    if (!state.showSearchExercise) {/*TODO*/
                    }
                }
            ) {
                Icon(
                    imageVector = Icons.Outlined.Delete,
                    contentDescription = "Delete Wk",
                    tint = colorError,
                    modifier = Modifier.padding(20.dp, end = 5.dp)
                )
                Text(text = "WIP", color = colorError, style = NormalTypo, fontSize = 14.sp)
            }
            IconButton(
                onClick = {/*TODO*/ },
                modifier = Modifier.padding(start = 10.dp)
            ) {
                Icon(
                    imageVector = Icons.Outlined.PersonOff,
                    tint = colorMessage,
                    contentDescription = "Toggle workout publicity"
                )
            }
            IconButton(
                onClick = {/*TODO*/ },
            ) {
                Icon(
                    imageVector = Icons.Outlined.Share,
                    tint = colorMessage,
                    contentDescription = "Share workout"
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            Box(
                modifier = Modifier
                    .padding(vertical = 10.dp)
                    .padding(end = 20.dp)
                    .clip(RoundedCornerShape(20))
                    .background(secondaryVariant)
            ) {
                TextButton(
                    enabled = !state.showSearchExercise,
                    onClick = { sendIntent(EditWkIntent.ShowSearchExercise(true)) }) {
                    MyRow {
                        Icon(
                            imageVector = Icons.Outlined.Add,
                            contentDescription = "Add Exercise",
                            tint = onSecondaryVariant
                        )
                    }
                    Text(
                        text = "ADD EXERCISE",
                        color = onSecondaryVariant,
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }
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
private fun EditWkElementComponent(
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
            .padding(horizontal = 10.dp, vertical = 6.dp)
            .border(2.dp, valueBoxColor.copy(alpha = 0.5f), RoundedCornerShape(15.dp))
            .clip(RoundedCornerShape(15.dp))
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
            color = nameColor
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
                        .fillMaxWidth()
                        .height(valueBoxSize)
                        .clip(RoundedCornerShape(5.dp))
                        .background(valueBoxColor),
                    contentAlignment = Alignment.Center
                ) {
                    BasicTextField(
                        singleLine = true,
                        enabled = true,
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
                    imageVector = Icons.Outlined.DragIndicator,
                    contentDescription = "Move Exercise",
                    tint = onTertiary
                )
            }
        }
    }
}


@Preview
@Composable
private fun ScreenPreview() {
    SalamandraTheme {
        ScreenBody(
            state = EditWkState.initial.copy(
                wkTemplate = WORKOUT_TEMPLATE,
            ),
            sendIntent = {},
        )
    }
}
