package com.android.salamandra.workouts.executeWk.presentation


import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Done
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.FitnessCenter
import androidx.compose.material.icons.outlined.PlayArrow
import androidx.compose.material.icons.outlined.Remove
import androidx.compose.material.icons.outlined.SkipNext
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.android.salamandra.R
import com.android.salamandra._core.domain.model.workout.executions.WkExecutionElement
import com.android.salamandra._core.presentation.asUiText
import com.android.salamandra._core.presentation.components.BottomSheet
import com.android.salamandra._core.presentation.components.EditWeight
import com.android.salamandra._core.presentation.components.ErrorDialog
import com.android.salamandra._core.presentation.components.ExerciseInfo
import com.android.salamandra._core.presentation.components.NumberField
import com.android.salamandra._core.presentation.components.TabRowBuilder
import com.android.salamandra._core.util.WK_EXECUTION_EXERCISE
import com.android.salamandra.destinations.HomeScreenDestination
import com.android.salamandra.ui.theme.SalamandraTheme
import com.android.salamandra.ui.theme.TitleTypo
import com.android.salamandra.ui.theme.colorMessage
import com.android.salamandra.ui.theme.onPrimary
import com.android.salamandra.ui.theme.onSecondary
import com.android.salamandra.ui.theme.onSecondaryVariant
import com.android.salamandra.ui.theme.onTertiary
import com.android.salamandra.ui.theme.primary
import com.android.salamandra.ui.theme.primaryVariant
import com.android.salamandra.ui.theme.secondaryVariant
import com.android.salamandra.ui.theme.tertiary
import com.android.salamandra.ui.theme.textFieldColors
import com.android.salamandra.ui.theme.title
import com.android.salamandra.workouts.executeWk.presentation.components.ExecuteWkScreenDestinations
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Destination(navArgsDelegate = ExecuteWkNavArgs::class)
@Composable
fun ExecuteWkScreen(
    navigator: DestinationsNavigator,
    viewModel: ExecuteWkViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    val events by viewModel.events.collectAsState(initial = null)
    LaunchedEffect(events) {
        when (events) {
            ExecuteWkEvent.EndWorkout -> navigator.navigate(HomeScreenDestination)
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
    state: ExecuteWkState,
    sendIntent: (ExecuteWkIntent) -> Unit
) {
    Scaffold(
        topBar = {
            ExecuteWkTabBar(
                activeTab = state.activeTab,
                setNumber = state.currentSet,
                onTabSelected = { sendIntent(ExecuteWkIntent.ChangeActiveTab(it)) }
            )
        }
    ) {
        if (state.activeTab == ExecuteWkScreenDestinations.ExecuteScreen) {
            ScreenBodyExecute(
                modifier = Modifier.padding(it),
                state = state,
                sendIntent = sendIntent
            )
        } else {
            ScreenBodyWkInfo(
                modifier = Modifier.padding(it),
                state = state,
                sendIntent = sendIntent
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ScreenBodyExecute(
    modifier: Modifier = Modifier,
    state: ExecuteWkState,
    sendIntent: (ExecuteWkIntent) -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(tertiary)
            .padding(horizontal = 12.dp)
            .padding(bottom = 12.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.align(Alignment.TopCenter),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ProgressBar(
                exerciseListSize = state.exerciseList.size,
                currentExerciseIndex = state.indexOfCurrentExercise
            )

            Text(
                text = state.exerciseList[state.indexOfCurrentExercise].exercise.name,
                color = title,
                fontWeight = FontWeight.Bold,
                fontSize = 26.sp
            )
            Spacer(Modifier.size(8.dp))
            Column(modifier = Modifier.weight(1f)) {
                Spacer(Modifier.weight(0.5f))
                OutlinedButton(
                    onClick = { sendIntent(ExecuteWkIntent.SkipSet) },
                    shape = RoundedCornerShape(30)
                ) {
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "Skip Set", color = onPrimary)
                        Icon(
                            imageVector = Icons.Outlined.SkipNext,
                            contentDescription = "Skip Set",
                            tint = onPrimary
                        )
                    }
                }
                Spacer(Modifier.weight(0.5f))
                state.exerciseList[state.indexOfCurrentExercise].executionElements.forEach { element ->
                    WkElementContainer(
                        wkExecutionElement = element,
                        currentSet = state.currentSet,
                        onEdit = { sendIntent(ExecuteWkIntent.ShowBottomSheet(element.setNumber)) }
                    )
                    Spacer(Modifier.weight(1f))
                }
            }
            BottomSection(
                currentSet = state.currentSet,
                executionElements = state.exerciseList[state.indexOfCurrentExercise].executionElements,
                onClickCheck = { sendIntent(ExecuteWkIntent.LogAction) }
            )
        }


        if (state.workoutEnded) {
            EndWorkoutScreen(
                surveyState = state.survey,
                totalExercises = state.exerciseList.size,
                onChangeSurveyToSad = { sendIntent(ExecuteWkIntent.ChangeSurveyToSad) },
                onChangeSurveyToNeutral = { sendIntent(ExecuteWkIntent.ChangeSurveyToNeutral) },
                onChangeSurveyToHappy = { sendIntent(ExecuteWkIntent.ChangeSurveyToHappy) },
                onEndWorkout = { sendIntent(ExecuteWkIntent.EndWorkout) }
            )
        }

        if (state.indexOfSelectedElement != null) {
            val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = false)
            BottomSheet(
                sheetState = sheetState,
                onDismiss = { sendIntent(ExecuteWkIntent.HideBottomSheet) },
                content = {
                    TabRowBuilder(
                        contents = listOf(
                            {
                                EditExecutionElement(
                                    exerciseName = state.exerciseList[state.indexOfCurrentExercise].exercise.name,
                                    element =state.exerciseList[state.indexOfCurrentExercise].executionElements[state.indexOfSelectedElement - 1],
                                    onEditWeight = { sendIntent(ExecuteWkIntent.EditWeight(it)) },
                                    onEditReps = { sendIntent(ExecuteWkIntent.EditReps(it)) },
                                    onEditRest = { sendIntent(ExecuteWkIntent.EditRest(it)) }
                                )
                            },
                            { ExerciseInfo(state.exerciseList[state.indexOfCurrentExercise].exercise) }
                        ),
                        icons = listOf(Icons.Outlined.Edit, Icons.Outlined.FitnessCenter),
                        titles = listOf("Edit", "Info")
                    )
                }
            )

        }

        if (state.error != null)
            ErrorDialog(
                error = state.error.asUiText(),
                onDismiss = { sendIntent(ExecuteWkIntent.CloseError) }
            )

    }
}

@Composable
private fun ScreenBodyWkInfo(
    modifier: Modifier = Modifier,
    state: ExecuteWkState,
    sendIntent: (ExecuteWkIntent) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(tertiary)
            .padding(12.dp),
        contentAlignment = Alignment.Center
    ) {
        Text("Work in progress", color = title, fontWeight = FontWeight.Bold)

    }

}

@Composable
private fun ProgressBar(
    exerciseListSize: Int,
    currentExerciseIndex: Int

) {
    Row(
        modifier = Modifier
            .padding(bottom = 8.dp)
            .padding(vertical = 12.dp)
    ) {
        for (i in 0..<exerciseListSize) {
            val color =
                if (i == currentExerciseIndex) primary else title
            Box(
                Modifier
                    .height(4.dp)
                    .background(color)
                    .weight(1f)
            )
            Spacer(Modifier.size(4.dp))
        }
    }
}


@Composable
private fun BottomSection(
    executionElements: List<WkExecutionElement>,
    onClickCheck: () -> Unit,
    currentSet: Int
) {
    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        val rest = executionElements[currentSet - 1].rest
        Spacer(Modifier.weight(1.5f))
        IconButton(modifier = Modifier
            .clip(RoundedCornerShape(30))
            .background(primary)
            .padding(12.dp),
            onClick = { onClickCheck() }
        ) {
            Icon(
                modifier = Modifier.size(36.dp),
                imageVector = Icons.Outlined.Done,
                contentDescription = "Set done",
                tint = onPrimary
            )
        }
        Spacer(Modifier.weight(0.1f))
        Text(
            modifier = Modifier
                .clip(CircleShape)
                .background(secondaryVariant)
                .padding(8.dp),
            text = "Rest: ${rest}s",
            color = onSecondaryVariant,
            fontSize = 22.sp
        )
        Spacer(Modifier.weight(0.05f))

    }
}

@Composable
private fun EndWorkoutScreen(
    surveyState: Int?,
    totalExercises: Int,
    onChangeSurveyToSad: () -> Unit,
    onChangeSurveyToNeutral: () -> Unit,
    onChangeSurveyToHappy: () -> Unit,
    onEndWorkout: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(tertiary),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Spacer(Modifier.weight(1f))
        Text(text = "Workout Ended", color = title, fontSize = 34.sp)
        Text(text = "How do you feel?", color = title, fontSize = 22.sp)
        Spacer(Modifier.size(12.dp))
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onChangeSurveyToSad) {
                Icon(
                    painter = painterResource(R.drawable.sad_face),
                    contentDescription = "bad",
                    tint = surveyIconColor(typeOfIcon = 0, surveyState = surveyState)
                )
            }
            IconButton(onClick = onChangeSurveyToNeutral) {
                Icon(
                    painter = painterResource(R.drawable.neutral_face),
                    contentDescription = "neutral",
                    tint = surveyIconColor(typeOfIcon = 1, surveyState = surveyState)
                )
            }
            IconButton(onClick = onChangeSurveyToHappy) {
                Icon(
                    painter = painterResource(R.drawable.happy_face),
                    contentDescription = "good",
                    tint = surveyIconColor(typeOfIcon = 2, surveyState = surveyState)
                )
            }
        }
        Spacer(Modifier.size(12.dp))
        Text(text = "$totalExercises exercises", color = title, fontSize = 19.sp)
        Spacer(Modifier.weight(1f))
        Button(onClick = onEndWorkout) {
            Text("End Workout", color = onPrimary)
        }

    }
}

private fun surveyIconColor(typeOfIcon: Int, surveyState: Int?) =
    if (typeOfIcon == surveyState) primary else onPrimary

@Composable
private fun WkElementContainer(
    wkExecutionElement: WkExecutionElement,
    currentSet: Int,
    onEdit: () -> Unit
) {
    val iconToShow: ImageVector
    val containerColor: Color
    if (wkExecutionElement.setNumber < currentSet) {
        iconToShow = Icons.Outlined.CheckCircle
        containerColor = colorMessage
    } else if (wkExecutionElement.setNumber == currentSet) {
        iconToShow = Icons.Outlined.PlayArrow
        containerColor = primary
    } else {
        iconToShow = Icons.Outlined.Remove
        containerColor = onSecondary
    }
    val stringToShow =
        "${wkExecutionElement.reps} reps" + if (wkExecutionElement.weight != null) " X ${wkExecutionElement.weight} Kg" else ""

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .border(width = 1.dp, color = containerColor, shape = RoundedCornerShape(40))
            .padding(8.dp),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            modifier = Modifier.size(32.dp),
            imageVector = iconToShow,
            contentDescription = null,
            tint = containerColor
        )
        Spacer(Modifier.weight(1f))
        Text(
            text = stringToShow,
            color = onPrimary,
            fontSize = 22.sp
        )
        Spacer(Modifier.weight(1f))
        Icon(
            modifier = Modifier
                .padding(end = 5.dp)
                .clickable { onEdit() },
            imageVector = Icons.Outlined.Edit,
            contentDescription = null,
            tint = containerColor.copy(0.4f),
        )

    }

}

@Composable
private fun ExecuteWkTabBar(
    activeTab: ExecuteWkScreenDestinations,
    setNumber: Int,
    onTabSelected: (ExecuteWkScreenDestinations) -> Unit

) {
    Row(
        modifier = Modifier
            .background(tertiary)
            .fillMaxWidth()
    ) {
        ExecuteWkScreenDestinations.entries.forEach { tab ->
            IndividualExecuteWkTab(
                text = tab.label,
                icon = tab.icon,
                onSelected = { onTabSelected(tab) },
                selected = tab == activeTab,
                extraText = if (tab == ExecuteWkScreenDestinations.ExecuteScreen) " #$setNumber" else ""
            )
        }
    }
}

@Composable
private fun IndividualExecuteWkTab(
    text: String,
    icon: ImageVector,
    onSelected: () -> Unit,
    selected: Boolean,
    extraText: String
) {
    val activeColor = title
    val inactiveColor = onTertiary
    val tabHeight = 24.dp

    val tabFadeInAnimationDuration = 150
    val tabFadeInAnimationDelay = 100
    val tabFadeOutAnimationDuration = 100

    val durationMillis = if (selected) tabFadeInAnimationDuration else tabFadeOutAnimationDuration
    val animSpec = remember {
        tween<Color>(
            durationMillis = durationMillis,
            easing = LinearEasing,
            delayMillis = tabFadeInAnimationDelay
        )
    }
    val tabTintColor by animateColorAsState(
        targetValue = if (selected) activeColor else inactiveColor,
        animationSpec = animSpec, label = "tab tint"
    )
    Row(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .padding(top = 16.dp)
            .animateContentSize()
            .height(tabHeight)
            .selectable(
                selected = selected,
                onClick = onSelected,
                role = Role.Tab,
                interactionSource = remember { MutableInteractionSource() },
                indication = rememberRipple(
                    bounded = false,
                    radius = Dp.Unspecified,
                    color = Color.Unspecified
                )
            )
            .clearAndSetSemantics { contentDescription = text }
    ) {
        Icon(imageVector = icon, contentDescription = text, tint = tabTintColor)
        if (selected) {
            Spacer(Modifier.width(10.dp))
            Text(
                text + extraText,
                color = tabTintColor,
                fontFamily = FontFamily.SansSerif,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Composable
fun EditExecutionElement(
    exerciseName: String,
    element: WkExecutionElement,
    onEditReps: (Int) -> Unit,
    onEditWeight: (Double) -> Unit,
    onEditRest: (Int) -> Unit,
) {
    Column(
        modifier = Modifier.imePadding()
    ) {
        Row {
            Text(
                text = exerciseName,
                fontSize = 22.sp,
                style = TitleTypo,
                color = title
            )
        }
        val wSpacer = 0.5f
        val wField = 1.1f
        val labelColor = onTertiary.copy(0.6f)
        Row(
            modifier = Modifier.padding(top = 20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(modifier = Modifier.weight(wSpacer))
            Box(modifier = Modifier.weight(wField)) {
                Text(text = stringResource(R.string.reps), color = labelColor)
            }
            Spacer(modifier = Modifier.weight(wSpacer))
            if (element.weight != null) {
                Box(modifier = Modifier.weight(wField)) {
                    Text(
                        text = stringResource(R.string.weight_kg),
                        color = labelColor
                    )
                }
                Spacer(modifier = Modifier.weight(wSpacer))
            }

        }
        Row(
            modifier = Modifier.padding(top = 5.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(modifier = Modifier.weight(wSpacer))
            Box(modifier = Modifier.weight(wField)) {
                NumberField(
                    modifier = Modifier.clip(RoundedCornerShape(10.dp)),
                    value = element.reps.toString(),
                    colors = if (element.reps != 0) {
                        textFieldColors(false)
                    } else {
                        textFieldColors(false).copy(
                            unfocusedTextColor = onTertiary,
                            focusedTextColor = onTertiary
                        )
                    },
                    onValueChange = {
                        val newInt = it.toIntOrNull() ?: 0
                        if (element.reps == 0 && newInt >= 10) {
                            onEditReps(newInt / 10)
                        } else {
                            onEditReps(newInt)
                        }
                    }
                )
            }
            if (element.weight != null) {
                Spacer(modifier = Modifier.weight(wSpacer))
                Row(
                    modifier = Modifier.weight(wField),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    EditWeight(
                        element.weight,
                        onEditWeight = { newWeight ->
                            onEditWeight(newWeight)
                        })
                }
            }
            Spacer(modifier = Modifier.weight(wSpacer))
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                modifier = Modifier.weight(wField),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = stringResource(R.string.rest), color = labelColor)
                val minutes = (element.rest / 60)
                val seconds = (element.rest % 60)
                Text(
                    modifier = Modifier.padding(start = 10.dp),
                    text = String.format("%d:%02d", minutes, seconds),
                    fontSize = 18.sp,
                    style = TitleTypo,
                )
            }
            Spacer(modifier = Modifier.weight(wSpacer))
            Box(modifier = Modifier.weight(2 * wField + wSpacer)) {
                val sliderPosition = remember { mutableFloatStateOf(element.rest.toFloat()) }
                Slider(
                    value = sliderPosition.value,
                    onValueChange = {
                        sliderPosition.value = it
                        onEditRest(it.toInt())
                    },
                    steps = 19,
                    valueRange = 0f..300f,
                    colors = SliderDefaults.colors(
                        thumbColor = primaryVariant,
                        activeTrackColor = primaryVariant,
                        activeTickColor = primary,
                    )
                )
            }
        }
    }
}


@Preview
@Composable
private fun ScreenExecutePreview() {
    SalamandraTheme {
        ScreenBody(
            state = ExecuteWkState.initial.copy(
                exerciseList = listOf(
                    WK_EXECUTION_EXERCISE,
                    WK_EXECUTION_EXERCISE.copy(exerciseNumber = 2),
                    WK_EXECUTION_EXERCISE.copy(exerciseNumber = 3)
                ),
                currentSet = 3,
                workoutEnded = false,
                survey = 1
            ),
            sendIntent = {}
        )
    }
}

