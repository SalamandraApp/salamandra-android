package com.android.salamandra.workouts.editWk.presentation

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.ArrowBackIosNew
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Construction
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Done
import androidx.compose.material.icons.outlined.DragIndicator
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.PublicOff
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.android.salamandra.R
import com.android.salamandra._core.domain.model.Exercise
import com.android.salamandra._core.domain.model.workout.WkTemplateElement
import com.android.salamandra._core.presentation.asUiText
import com.android.salamandra._core.presentation.components.ErrorDialog
import com.android.salamandra._core.presentation.components.MyColumn
import com.android.salamandra._core.presentation.components.MyRow
import com.android.salamandra._core.presentation.components.MySpacer
import com.android.salamandra._core.presentation.components.ProfilePicture
import com.android.salamandra._core.presentation.components.WkPlaceholder
import com.android.salamandra._core.util.EXERCISE_LIST
import com.android.salamandra._core.util.LONG_EXERCISE_LIST
import com.android.salamandra._core.util.WORKOUT_TEMPLATE
import com.android.salamandra.destinations.HomeScreenDestination
import com.android.salamandra.home.presentation.HomeIntent
import com.android.salamandra.home.presentation.components.SearchScreen
import com.android.salamandra.ui.theme.NormalTypo
import com.android.salamandra.ui.theme.SalamandraTheme
import com.android.salamandra.ui.theme.SemiTypo
import com.android.salamandra.ui.theme.TitleTypo
import com.android.salamandra.ui.theme.WkTemplateElementTypo
import com.android.salamandra.ui.theme.colorError
import com.android.salamandra.ui.theme.onPrimary
import com.android.salamandra.ui.theme.onSecondary
import com.android.salamandra.ui.theme.onSecondaryVariant
import com.android.salamandra.ui.theme.onTertiary
import com.android.salamandra.ui.theme.primary
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
    sendIntent: (EditWkIntent) -> Unit
) {
    val mainColor = tertiary
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(mainColor)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val columnWeights: FloatArray = floatArrayOf(0.5f, 0.1f, 0.1f, 0.15f, 0.1f)
            val startPad = 15.dp
            MyEditWkBanner(
                state = state,
                sendIntent = sendIntent,
                backgroundColor = mainColor,
                startPad = startPad,
                columnWeightVector = columnWeights)
            MyFadeLip(backgroundColor = mainColor)
            Spacer(modifier = Modifier.size(5.dp))
            LazyColumn(
                modifier = Modifier
                    .background(mainColor)
                    .fillMaxWidth()
            ) {
                items(state.wkTemplate.elements) {
                    WkElementComponent(
                        wkElement = it,
                        fgColor = secondary,
                        startPad = startPad,
                        columnWeightVector = columnWeights)
                }
            }
        }
    }
}


@Composable
fun MyFadeLip(backgroundColor: Color) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(6.dp)
            .background(backgroundColor)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(6.dp)
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color.Black.copy(alpha = 0.6f),
                            Color.Black.copy(alpha = 0.0f)
                        )
                    )
                )
        ) {}
    }
}

@Composable
fun MyEditWkBanner(
    state: EditWkState,
    sendIntent: (EditWkIntent) -> Unit,
    columnWeightVector: FloatArray,
    startPad: Dp,
    backgroundColor: Color) {
    if (columnWeightVector.size != 5) {
        throw IllegalArgumentException("The length of the float array must be 5.")
    }
    Column (
        modifier = Modifier
            .fillMaxWidth()
            .background(backgroundColor)

    ) {
        Row (
            modifier = Modifier
                .fillMaxWidth()
                .height(55.dp)
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
        val picSize = 120
        val sideMargin = 20
        val topMargin = sideMargin/2;
        Row (
            modifier = Modifier
                .fillMaxWidth()
                .height((picSize + 2 * topMargin).dp)
                .align(Alignment.Start),
        )
        {
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(1f),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.Start
            ) {
                Box (
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
                val textColor = if (description.isNullOrEmpty()) title.copy(alpha = 0.5f) else title
                Box(
                    modifier = Modifier
                        .padding(horizontal = sideMargin.dp)
                        .padding(bottom = topMargin.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(secondary)
                        .fillMaxWidth()
                        .fillMaxHeight()

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

            }

            Column(
                modifier = Modifier
                    .width((picSize + 2 * sideMargin).dp)
                    .padding(horizontal = sideMargin.dp)
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                WkPlaceholder(modifier = Modifier, size = picSize, shape = RoundedCornerShape(5))
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .align(Alignment.End),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Outlined.Construction,
                contentDescription = "Construction icon",
                modifier = Modifier.size(20.dp),
                tint = primaryVariant,
            )
            Text(
                text = "WIP",
                color = primaryVariant,
                fontSize = 18.sp,
                modifier = Modifier.padding(start = 8.dp),
                style = TitleTypo,
            )
        }
        Row (
            modifier = Modifier
                .fillMaxWidth()
                .height(65.dp)
                .align(Alignment.Start),
            verticalAlignment = Alignment.CenterVertically
        )
        {
            MyRow(
                modifier = Modifier.clickable
                { if (!state.showSearchExercise) {/*TODO*/} }
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
                    imageVector = Icons.Outlined.PublicOff,
                    tint = onTertiary,
                    contentDescription = "Search workout"
                )
            }
            IconButton(
                onClick = {/*TODO*/ },
            ) {
                Icon(
                    imageVector = Icons.Outlined.Share,
                    tint = onTertiary,
                    contentDescription = "Search workout"
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            Box (
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
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(30.dp)
                .padding(start = 10.dp, end = 10.dp, bottom = 6.dp)
                .align(Alignment.Start),
            verticalAlignment = Alignment.Bottom
        ) {
            val columnLabelColor = onTertiary.copy(alpha = 0.6f)
            // EXERCISE NAME
            Text(
                modifier = Modifier
                    .weight(columnWeightVector[0])
                    .padding(start = 10.dp),
                text = stringResource(R.string.exercise),
                color = columnLabelColor,
                fontSize = 14.sp,
                style = SemiTypo
            )
            // SETS
            Text(
                modifier = Modifier
                    .weight(columnWeightVector[1]),
                text = stringResource(R.string.sets),
                color = columnLabelColor,
                fontSize = 14.sp,
                style = SemiTypo
            )
            // REPS
            Text(
                modifier = Modifier
                    .weight(columnWeightVector[2]),
                text = stringResource(R.string.reps),
                color = columnLabelColor,
                fontSize = 14.sp,
                style = SemiTypo
            )
            // WEIGHT
            Text(
                modifier = Modifier
                    .weight(columnWeightVector[3] + columnWeightVector[4]),
                text = stringResource(R.string.weight_kg),
                color = columnLabelColor,
                fontSize = 14.sp,
                style = SemiTypo
            )
        }
    }
}


@Composable
private fun WkElementComponent(
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
//                showSearchExercise = true,
                exerciseList = EXERCISE_LIST
            ),
            sendIntent = {}
        )
    }
}
