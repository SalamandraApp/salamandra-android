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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.FolderOpen
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.FitnessCenter
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import com.android.salamandra.R
import com.android.salamandra._core.domain.model.workout.WkTemplateElement
import com.android.salamandra._core.presentation.components.BottomSheet
import com.android.salamandra._core.presentation.components.ExerciseInfo
import com.android.salamandra._core.presentation.components.FadeLip
import com.android.salamandra._core.presentation.components.TabRowBuilder
import com.android.salamandra._core.presentation.components.WkElementComponent
import com.android.salamandra._core.presentation.components.WkTemplatePicture
import com.android.salamandra._core.presentation.components.WkTemplateViewLabels
import com.android.salamandra._core.util.WORKOUT_TEMPLATE
import com.android.salamandra._core.util.WORKOUT_TEMPLATE_ELEMENT
import com.android.salamandra.ui.theme.NormalTypo
import com.android.salamandra.ui.theme.SemiTypo
import com.android.salamandra.ui.theme.TitleTypo
import com.android.salamandra.ui.theme.colorError
import com.android.salamandra.ui.theme.onTertiary
import com.android.salamandra.ui.theme.primaryVariant
import com.android.salamandra.ui.theme.secondary
import com.android.salamandra.ui.theme.tertiary
import com.android.salamandra.ui.theme.textFieldColors
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
            EditWkEvent.NavigateUp -> navigator.navigateUp()
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
    val bannerHeight = 320.dp
    val fixedBannerHeight = 70.dp

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
                    bgColor = tertiary
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
            itemsIndexed(state.wkTemplate.elements) { index, element ->
                WkElementComponent(
                    wkElement = element,
                    fgColor = secondary,
                    startPad = startPad,
                    onOption = {sendIntent(EditWkIntent.ShowBottomSheet(index))},
                    columnWeightVector = columnWeights,
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditExercise(
    // TODO, delete placeholder
    templateElement: WkTemplateElement = WORKOUT_TEMPLATE_ELEMENT,
    index: Int,
    sendIntent: (EditWkIntent) -> Unit
) {

    val keyboardOptions = KeyboardOptions.Default.copy(
        keyboardType = KeyboardType.Number
    )
    Row {
        Text(
            text = templateElement.exercise.name,
            fontSize = 22.sp,
            style = TitleTypo,
            color = title
        )
    }
    val wSpacer = 0.3f
    val wField = 1f
    val labelColor = onTertiary.copy(0.6f)
    Row (
        modifier = Modifier.padding(top = 20.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box (modifier = Modifier.weight(wField)){
            Text(text = stringResource(R.string.sets), color = labelColor)
        }
        Spacer (modifier = Modifier.weight(wSpacer))
        Box (modifier = Modifier.weight(wField)){
            Text(text = stringResource(R.string.reps), color = labelColor)
        }
        Spacer (modifier = Modifier.weight(wSpacer))
        Box (modifier = Modifier.weight(wField + wSpacer/2)){
            Text(text = stringResource(R.string.weight_kg), color = labelColor)
        }

    }
    Row (
        modifier = Modifier.padding(top = 5.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box (
            modifier = Modifier.weight(wField)
        ){
            TextField(
                modifier = Modifier.clip(RoundedCornerShape(10.dp)),
                singleLine = true,
                enabled = true,
                value = templateElement.sets.toString(),
                textStyle = TitleTypo.copy(fontSize = 20.sp),
                colors = textFieldColors(),
                keyboardOptions = keyboardOptions,
                onValueChange = {
                    val newSets = it.toIntOrNull()
                    if (newSets != null) {
                        // sendIntent(EditWkIntent.ChangeWkElementSets(newSets, index))
                    }
                }
            )
        }
        Icon(
            modifier = Modifier.weight(wSpacer),
            imageVector = Icons.Outlined.Close,
            tint = onTertiary,
            contentDescription = "Search workout"
        )
        Box (modifier = Modifier.weight(wField)){
            TextField(
                modifier = Modifier.clip(RoundedCornerShape(10.dp)),
                singleLine = true,
                enabled = true,
                value = templateElement.reps.toString(),
                textStyle = TitleTypo.copy(fontSize = 20.sp),
                colors = textFieldColors(),
                keyboardOptions = keyboardOptions,
                onValueChange = {
                    val newReps = it.toIntOrNull()
                    if (newReps != null) {
                        // sendIntent(EditWkIntent.ChangeWkElementReps(newReps, index))
                    }
                }
            )
        }
        Spacer(modifier = Modifier.weight(wSpacer/2))
        Box (
            modifier = Modifier.weight(wSpacer/2),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "(",
                fontSize = 26.sp,
                color = onTertiary
            )
        }
        Row (
            modifier = Modifier.weight(wField),
            verticalAlignment = Alignment.CenterVertically
        ){
            TextField(
                modifier = Modifier
                    .clip(RoundedCornerShape(10.dp)),
                singleLine = true,
                enabled = true,
                value = templateElement.weight.toString(),
                textStyle = TitleTypo.copy(fontSize = 20.sp),
                colors = textFieldColors(),
                keyboardOptions = keyboardOptions,
                onValueChange = {
                    val newWeight= it.toDoubleOrNull()
                    if (newWeight != null) {
                        // sendIntent(EditWkIntent.ChangeWkElementWeight(newWeight, index))
                    }
                }
            )

        }
        Box (
            modifier = Modifier.weight(wSpacer/2),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = ")",
                fontSize = 26.sp,
                color = onTertiary
            )
        }

    }

    Row (
        modifier = Modifier.padding(top = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        val checkColor = onTertiary.copy(0.7f)
        val checkedReps = remember { mutableStateOf(false) }
        val checkedWeight = remember { mutableStateOf(false) }
        val checkBoxColors = CheckboxDefaults.colors(
            checkedColor = checkColor,
            uncheckedColor = checkColor,
            checkmarkColor = tertiary
        )
        Text(
            modifier = Modifier.weight(wField + wSpacer),
            text = stringResource(R.string.change_through_set),
            style = NormalTypo,
            color = checkColor,
            fontSize = 15.sp
        )
        Checkbox(
            modifier = Modifier.weight(wField),
            checked = checkedReps.value,
            onCheckedChange = { checkedReps.value = it },
            colors = checkBoxColors
        )
        Spacer(modifier = Modifier.weight(wSpacer))
        Checkbox(
            modifier = Modifier.weight(wField),
            checked = checkedWeight.value,
            onCheckedChange = { checkedWeight.value = it },
            colors = checkBoxColors
        )
        Spacer(modifier = Modifier.weight(wSpacer/2))

    }

    Row (
        modifier = Modifier.padding(top = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(modifier = Modifier.weight(wField)) {
            Text(text = stringResource(R.string.rest), color = labelColor)
        }
    }
    Row (
      modifier = Modifier.padding(top = 5.dp)
    ) {
        // TODO, placeholder
        val time = remember { mutableStateOf("0") }
        TextField(
            modifier = Modifier
                .weight(1f)
                .clip(RoundedCornerShape(10.dp)),
            singleLine = true,
            enabled = true,
            value = time.value,
            // value = templateElement.rest.toString(),
            textStyle = TitleTypo.copy(fontSize = 20.sp),
            colors = textFieldColors(),
            keyboardOptions = keyboardOptions,
            onValueChange = {
            },
            placeholder = { Text("MM:SS") }
        )
        Spacer(modifier = Modifier.weight(wSpacer))
        Box(modifier = Modifier.weight(2 * wField + wSpacer)) {
            ExtendedFloatingActionButton(
                containerColor = secondary,
                contentColor = colorError,
                elevation = FloatingActionButtonDefaults.elevation(8.dp),
                onClick = { }) {
                Icon(
                    imageVector = Icons.Outlined.Delete,
                    contentDescription = "Delete exercise",
                )
                Text(
                    text = stringResource(R.string.delete_exercise),
                    modifier = Modifier.padding(start = 8.dp)
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
    bgColor: Color) {

    val dpSideMargin = 20.dp
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(bgColor)
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
        WkTemplateViewLabels(
            columnWeightVector = columnWeightVector
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
            .background(backgroundColor)
            .padding(horizontal = dpSideMargin)
    ) {
        EditWkBannerTopRow(
            modifier = Modifier
                .weight(wTopRow),
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
    state: EditWkState,
    sendIntent: (EditWkIntent) -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    )
    {
        Icon(
            imageVector = Icons.Outlined.Delete,
            contentDescription = "Delete Wk",
            tint = colorError,
        )
        Text(
            modifier = Modifier.padding(start = 5.dp),
            text = "Delete",
            color = colorError,
            style = SemiTypo,
            fontSize = 14.sp
        )
        IconButton(
            onClick = { sendIntent(EditWkIntent.ShowBottomSheet(0)) },
            modifier = Modifier.padding(start = 10.dp)
        ) {
            Icon(
                imageVector = Icons.Filled.FolderOpen,
                tint = onTertiary,
                contentDescription = "Folder workout"
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        ExtendedFloatingActionButton(
            containerColor = primaryVariant.copy(0.3f),
            contentColor = primaryVariant,
            elevation = FloatingActionButtonDefaults.elevation(8.dp),
            onClick = { sendIntent(EditWkIntent.ShowSearchExercise(true)) }) {
            Icon(
                imageVector = Icons.Outlined.Add,
                contentDescription = "Add Exercise",
            )
            Text(
                text = "ADD EXERCISE",
                modifier = Modifier.padding(start = 8.dp)
            )
        }
    }
}

@Composable
private fun BannerTitleRow(
    modifier: Modifier = Modifier,
    sendIntent: (EditWkIntent) -> Unit,
    state: EditWkState
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
            TextField(
                modifier = Modifier.clip(RoundedCornerShape(10.dp)),
                singleLine = true,
                enabled = true,
                value = state.wkTemplate.name,
                textStyle = TitleTypo.copy(fontSize = 16.sp),
                colors = textFieldColors(),
                onValueChange = { sendIntent(EditWkIntent.ChangeWkName(it)) }
            )
            Spacer(modifier = Modifier.weight(1f))

            val textToShow = state.wkTemplate.description ?: ""
            TextField(
                modifier = Modifier.clip(RoundedCornerShape(10.dp)),
                enabled = true,
                value = textToShow,
                placeholder = {
                    if (textToShow.isEmpty()) {
                        Text("Description...", style = TitleTypo)
                    }
                },
                minLines = 2,
                maxLines = 2,
                textStyle = TitleTypo.copy(fontSize = 14.sp),
                colors = textFieldColors(),
                onValueChange = { sendIntent(EditWkIntent.ChangeWkDescription(it)) },
            )

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
private fun EditWkBannerTopRow (
    modifier: Modifier = Modifier,
    sendIntent: (EditWkIntent) -> Unit,
    state: EditWkState,
    middleContent: @Composable () -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    )
    {
        Box(modifier = Modifier
            .clickable { sendIntent(EditWkIntent.NavigateUp) }
        ) {
            Icon(
                imageVector = Icons.Outlined.Close,
                tint = onTertiary,
                contentDescription = "Search workout"
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        middleContent()
        Spacer(modifier = Modifier.weight(1f))
        Box(modifier = Modifier
            .clickable { /*TODO*/ }
        ) {
            Icon(
                modifier = Modifier.size(27.dp),
                tint = primaryVariant,
                imageVector = Icons.Outlined.CheckCircle,
                contentDescription = "Search workout"
            )
        }
    }
}

@Composable
private fun EditTagRow(
    modifier: Modifier = Modifier,
    sendIntent: (EditWkIntent) -> Unit,
    state: EditWkState
) {
    Row (
        modifier = modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        val wTags = 800f
        val wButton = 200f
        Box (
            modifier = Modifier
                .weight(wTags)
                .padding(end = 15.dp)
        ) {
            val borderColor = onTertiary.copy(0.8f)
            val items = listOf("#mytag1", "#tag2", "#thistag3", "#tag4")
            LazyRow {
                items(items.size) { index ->
                    Row(
                        modifier = Modifier
                            .fillMaxHeight()
                            .padding(end = if (index == items.size - 1) 0.dp else 10.dp)
                            .border(1.dp, borderColor, shape = RoundedCornerShape(50)),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Box(modifier = Modifier
                            .padding(horizontal = 5.dp)
                            .clickable { }
                        ) {
                            Icon(
                                modifier = Modifier.size(15.dp),
                                imageVector = Icons.Filled.Close,
                                tint = onTertiary,
                                contentDescription = "Leading Icon"
                            )
                        }
                        Text(
                            text = items[index],
                            color = onTertiary,
                            style = SemiTypo,
                            fontSize = 13.sp
                        )
                        Box(modifier = Modifier
                            .padding(horizontal = 5.dp)
                            .clickable { }
                        ) {
                            Icon(
                                modifier = Modifier.size(15.dp),
                                imageVector = Icons.Filled.Edit,
                                tint = onTertiary,
                                contentDescription = "Trailing Icon"
                            )
                        }
                    }
                }
            }
            if (items.size >=3)
                FadeLip(vertical = true, modifier = Modifier.align(Alignment.CenterEnd))
        }
        FloatingActionButton(
            modifier = Modifier.weight(wButton),
            containerColor = secondary,
            contentColor = primaryVariant,
            elevation = FloatingActionButtonDefaults.elevation(8.dp),
            onClick = { sendIntent(EditWkIntent.ShowSearchExercise(true)) }) {
            Icon(
                imageVector = Icons.Outlined.Add,
                contentDescription = "Add Exercise",
            )
        }
    }
}


@Preview
@Composable
private fun MainBannerRowPreview() {
    Column (
        modifier = Modifier
            .height(150.dp)
            .background(tertiary)
    ) {
        BannerTitleRow(
            sendIntent = {},
            state = EditWkState.initial.copy(wkTemplate = WORKOUT_TEMPLATE)
        )
    }
}


@Preview
@Composable
private fun EditWkFixedBannerPreview() {
    val columnWeights: FloatArray = floatArrayOf(0.5f, 0.1f, 0.1f, 0.15f, 0.1f)
    EditWkFixedBanner(
        modifier = Modifier.height(70.dp),
        state = EditWkState.initial.copy(wkTemplate = WORKOUT_TEMPLATE),
        sendIntent = {},
        columnWeightVector = columnWeights,
        bgColor = tertiary
    )
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