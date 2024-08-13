package com.android.salamandra.workouts.executeWk

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Done
import androidx.compose.material.icons.outlined.PlayArrow
import androidx.compose.material.icons.outlined.Remove
import androidx.compose.material.icons.outlined.SkipNext
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.android.salamandra.R
import com.android.salamandra._core.domain.model.workout.executions.WkExecutionElement
import com.android.salamandra._core.presentation.asUiText
import com.android.salamandra._core.presentation.components.ErrorDialog
import com.android.salamandra._core.util.WK_EXECUTION_EXERCISE
import com.android.salamandra.destinations.HomeScreenDestination
import com.android.salamandra.ui.theme.SalamandraTheme
import com.android.salamandra.ui.theme.colorMessage
import com.android.salamandra.ui.theme.onPrimary
import com.android.salamandra.ui.theme.onSecondary
import com.android.salamandra.ui.theme.primary
import com.android.salamandra.ui.theme.tertiary
import com.android.salamandra.ui.theme.title
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
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(tertiary)
            .padding(12.dp),
        contentAlignment = Alignment.Center
    ) {
        if (state.currentExercise != null) {
            Column(
                modifier = Modifier.align(Alignment.TopCenter),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier
                        .padding(bottom = 8.dp)
                        .padding(vertical = 12.dp)
                ) {
                    for (i in 0..<state.exerciseList.size) {
                        val color =
                            if (i == state.exerciseList.indexOf(state.currentExercise)) primary else title
                        Box(
                            Modifier
                                .height(4.dp)
                                .background(color)
                                .weight(1f)
                        )
                        Spacer(Modifier.size(4.dp))
                    }
                }
                Text(
                    text = state.currentExercise.exercise.name,
                    color = title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 26.sp
                )
                Spacer(Modifier.size(8.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Spacer(Modifier.weight(0.5f))
                    OutlinedButton(onClick = {/*TODO*/ }, shape = RoundedCornerShape(30)) {
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
                    state.currentExercise.executionElements.forEach { element ->
                        WkElementContainer(element, state.currentSet)
                        Spacer(Modifier.weight(1f))
                    }
                }
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val rest = state.currentExercise.executionElements[state.currentSet - 1].rest
                    Spacer(Modifier.weight(1.5f))
                    IconButton(modifier = Modifier
                        .clip(RoundedCornerShape(30))
                        .background(primary)
                        .padding(12.dp),
                        onClick = { sendIntent(ExecuteWkIntent.LogAction) }
                    ) {
                        Icon(
                            modifier = Modifier.size(36.dp),
                            imageVector = Icons.Outlined.Done,
                            contentDescription = "Set done",
                            tint = onPrimary
                        )
                    }
                    Spacer(Modifier.weight(0.25f))
                    Text(
                        text = "Rest: ${rest}s",
                        color = onPrimary,
                        fontSize = 22.sp
                    )
                    Spacer(Modifier.weight(0.1f))

                }

            }

        }

        if (state.workoutEnded) {
            EndWorkoutScreen(
                state.survey,
                onChangeSurveyToSad = { sendIntent(ExecuteWkIntent.ChangeSurveyToSad) },
                onChangeSurveyToNeutral = { sendIntent(ExecuteWkIntent.ChangeSurveyToNeutral) },
                onChangeSurveyToHappy = { sendIntent(ExecuteWkIntent.ChangeSurveyToHappy) },
                onEndWorkout = { sendIntent(ExecuteWkIntent.EndWorkout) }
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
private fun EndWorkoutScreen(
    surveyState: Int?,
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
        Spacer(Modifier.weight(1f))
        Button(onClick = onEndWorkout) {
            Text("End Workout", color = onPrimary)
        }

    }
}

private fun surveyIconColor(typeOfIcon: Int, surveyState: Int?) =
    if (typeOfIcon == surveyState) primary else onPrimary

@Composable
private fun WkElementContainer(wkExecutionElement: WkExecutionElement, currentSet: Int) {
    val iconToShow: ImageVector
    val containerColor: Color
    if (wkExecutionElement.currentRep < currentSet) {
        iconToShow = Icons.Outlined.CheckCircle
        containerColor = colorMessage
    } else if (wkExecutionElement.currentRep == currentSet) {
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
    }

}

@Preview()
@Composable
private fun ScreenPreview() {
    SalamandraTheme {
        ScreenBody(
            state = ExecuteWkState.initial.copy(
                exerciseList = listOf(
                    WK_EXECUTION_EXERCISE,
                    WK_EXECUTION_EXERCISE.copy(setNumber = 2),
                    WK_EXECUTION_EXERCISE.copy(setNumber = 3)
                ),
                currentExercise = WK_EXECUTION_EXERCISE,
                currentSet = 3,
                workoutEnded = false,
                survey = 1
            ),
            sendIntent = {}
        )
    }
}
